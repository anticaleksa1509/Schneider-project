package raf.rs.SportsBooking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.rs.SportsBooking.dto.SessionDTO;
import raf.rs.SportsBooking.model.Coach;
import raf.rs.SportsBooking.model.Field;
import raf.rs.SportsBooking.model.Session;
import raf.rs.SportsBooking.model.SportsGroup;
import raf.rs.SportsBooking.repository.CoachRepo;
import raf.rs.SportsBooking.repository.FieldRepo;
import raf.rs.SportsBooking.repository.SessionRepo;
import raf.rs.SportsBooking.repository.SportsGroupRepo;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class SessionService {

    
    private final CoachRepo coachRepo;
    private final FieldRepo fieldRepo;
    private final SportsGroupRepo sportsGroupRepo;
    private final SessionRepo sessionRepo;
    private final TaskScheduler taskScheduler;

    @Transactional
    public void scheduleSession(LocalDateTime time, SessionDTO sessionDTO) throws Exception {

        Date date = java.sql.Timestamp.valueOf(time);

        Optional<List<Session>> listOfSessions = sessionRepo.findAllByTypeOfSessionAndDayOfWeek
                (sessionDTO.getTypeOfSession(),sessionDTO.getDayOfWeek());

        if(listOfSessions.isEmpty())
            try {
                throw new Exception("There are no available sessions");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        List<Session> sessions = listOfSessions.get();
        Session availableSession = sessions.stream().filter(s -> checkAvailability(s, sessionDTO)).findFirst().orElse(null);

        //prvi slobodan termin
        if(availableSession == null)
                throw new Exception("There are no available session!");

        taskScheduler.schedule(()->{

            ReentrantLock reentrantLock = new ReentrantLock();
            try {
                reentrantLock.lock();
                availableSession.setReserved(true);
                sessionRepo.save(availableSession);
                // moramo asihnrono da pozovem da se nakon kraja termina
                // promeni status opet na false da nije rezervisano kada se zavrsi termin
            }finally {
                reentrantLock.unlock();
            }
        },date);

    }
    public Boolean checkAvailability(Session s,SessionDTO sessionDTO){
        if(!s.isReserved() && (s.getField().getCapacity() >= sessionDTO.getNumberOfPlayers())){
            if(checkTimeForSession(s,sessionDTO))
                return true;
        }
        return false;
    }
    public Boolean checkTimeForSession(Session s,SessionDTO sessionDTO){
        if((sessionDTO.getStartTime() == s.getStartTime()) && (sessionDTO.getEndTime() == s.getEndTime()) ||
                (sessionDTO.getStartTime().isAfter(s.getStartTime()) && sessionDTO.getStartTime().isBefore(s.getEndTime()) && sessionDTO.getEndTime().isBefore(s.getEndTime()))){
            return true;
        }
        return false;
    }
    public List<Session> getAllSessions(){
        return sessionRepo.findAll();
    }
    public List<Session> getByGroupType(String type) throws Exception{
        Optional<List<Session>> optionalSessions = sessionRepo.findAllBySportsGroup_Type(type);
        if(optionalSessions.isPresent() && !optionalSessions.get().isEmpty()) {
            List<Session> sessions = optionalSessions.get();
            return sessions;
        }else {
            throw new Exception("There are no sessions with that type of group");
        }
    }
    public List<Session> getSessionsSortedByFieldMarkAsc() {
        Sort sort = Sort.by(Sort.Order.asc("field.mark")); // Sortiranje po imenu terena
        return sessionRepo.findAll(sort); // Preuzimanje svih sesija sa sortiranjem
    }
    public List<Session> getSessionByDayAndTime(DayOfWeek dayOfWeek) throws Exception{
        Sort sort = Sort.by(Sort.Order.asc("startTime"));
        Optional<List<Session>> optionalSession = sessionRepo.findAllByDayOfWeek(dayOfWeek,sort);
        if(optionalSession.isPresent() && !optionalSession.get().isEmpty())
            return optionalSession.get();
        throw new Exception("There are no sessions for the particular day");
    }

    public List<Session> getSessionsSortedByFieldMarkDesc() {
        Sort sort = Sort.by(Sort.Order.desc("field.mark")); // Sortiranje po imenu terena
        return sessionRepo.findAll(sort); // Preuzimanje svih sesija sa sortiranjem
    }

    public List<Session> getSessionByType(String typeOfSession) throws Exception {
        Optional<List<Session>> sessionList = sessionRepo.findAllByTypeOfSession(typeOfSession);
        if(sessionList.isPresent() && !sessionList.get().isEmpty()){
            return sessionList.get();
        }
        throw new Exception("There are no sessions with the type you entered");
    }
    public List<Session> allByDayAndField(DayOfWeek dayOfWeek, Long id_field) throws Exception{
        Optional<Field> optionalField = Optional.ofNullable(fieldRepo.findById(id_field).orElseThrow(() ->
                new EntityNotFoundException("Field with the ID you entered does not exist")));
        Field field = optionalField.get();
        Optional<List<Session>> sessionList = sessionRepo.findAllByDayOfWeekAndField(dayOfWeek,field);
        if(sessionList.isPresent() && !sessionList.get().isEmpty())
            return sessionList.get();
        throw new Exception("There are no specific session with field and day you entered");
    }
    public List<Session> getAllByFieldCapacity(){
        Sort sort = Sort.by(Sort.Order.asc("field.capacity"));
        return sessionRepo.findAll(sort);
    }
    public Session getSessionByParameters(DayOfWeek dayOfWeek, LocalTime startTime,Long id_field) throws Exception {
        Optional<Field> optionalField = fieldRepo.findById(id_field);
        if (optionalField.isEmpty())
            throw new Exception("Field ID u entered is not valid or doesn't exist");
        Field field = optionalField.get();
        if(!(dayOfWeek != null && startTime != null))
            throw new Exception("All fields must be covered");
        Optional<Session> session = sessionRepo.findSessionByFieldAndDayOfWeekAndStartTime(field,dayOfWeek,startTime);
        if(session.isPresent())
            return session.get();
        throw new Exception("There is not session with parameters that u have entered");
    }

    public void deleteSession(Session session){
        sessionRepo.delete(session);
    }

    public Session getSessionByCoach(Long id_coach) throws Exception {
        Optional<Coach> optionalCoach = coachRepo.findById(id_coach);
        if (optionalCoach.isPresent()){
            Coach coach = optionalCoach.get();
            Optional<Session> optionalSession = sessionRepo.findSessionByCoach(coach);
            if(optionalSession.isPresent())
                return optionalSession.get();
        }
        throw new Exception("Session with the coach u are searching for does not exist!");

    }

    public Session getSessionByGroup(Long id_group) throws Exception {
        Optional<SportsGroup> optionalSportsGroup = sportsGroupRepo.findById(id_group);
        if(optionalSportsGroup.isPresent()) {
            SportsGroup sportsGroup = optionalSportsGroup.get();
            Optional<Session> optionalSession = sessionRepo.findSessionBySportsGroup(sportsGroup);
            if(optionalSession.isPresent())
                return optionalSession.get();
            throw new Exception("Session with the sports group that you are searching for is not available" +
                    "or does not exist");
        }else {
            throw new Exception("Group with the ID you entered does not exist");
        }

    }

    public Session getSessionByField(Long id_field) throws Exception {
        Optional<Field> optionalField = fieldRepo.findById(id_field);
        if(!optionalField.isPresent())
            throw new Exception("Field with that ID does not exist");
        Optional<Session> optionalSession = sessionRepo.findSessionByField(optionalField.get());
        if(optionalSession.isPresent())
            return optionalSession.get();
        throw new Exception("Session with field you entered does not exist");
    }

    public String createSession(Long id_coach, Long id_group, Long id_field, Session session) throws Exception {

        Optional<Coach> optionalCoach = coachRepo.findById(id_coach);
        Optional<Field> optionalField = fieldRepo.findById(id_field);
        Optional<SportsGroup> optionalSportsGroup = sportsGroupRepo.findById(id_group);
        if (optionalCoach.isEmpty() || optionalField.isEmpty() || optionalSportsGroup.isEmpty())
            throw new Exception("Some of the ID parameters you entered are invalid or do not exist.");

        Coach coach = optionalCoach.get();
        session.setCoach(coach);
        Field field = optionalField.get();
        session.setField(field);
        SportsGroup sportsGroup = optionalSportsGroup.get();
        session.setSportsGroup(sportsGroup);

        List<Session> sessions = sessionRepo.findAll();

        for (Session session1 : sessions) {
            if (session1.getDayOfWeek().equals(session.getDayOfWeek())) {
                if (session1.getField().equals(session.getField())) {
                    if((session.getStartTime().isAfter(session1.getStartTime())
                            && session.getStartTime().isBefore(session1.getEndTime())) ||
                            (session.getEndTime().isAfter(session1.getStartTime()) && session.getEndTime().isBefore(session1.getEndTime()))
                            || (session.getStartTime().equals(session1.getStartTime()) && session.getEndTime().equals(session1.getEndTime()))
                || (session.getStartTime().isBefore(session1.getStartTime())) && session.getEndTime().isAfter(session1.getEndTime()))
                        throw new Exception("Field is already booked at that time");

                }if(session1.getCoach().equals(session.getCoach())){
                    if((session.getStartTime().isAfter(session1.getStartTime())
                            && session.getStartTime().isBefore(session1.getEndTime())) ||
                            (session.getEndTime().isAfter(session1.getStartTime()) && session.getEndTime().isBefore(session1.getEndTime()))
                            || (session.getStartTime().equals(session1.getStartTime()) && session.getEndTime().equals(session1.getEndTime()))
                            || (session.getStartTime().isBefore(session1.getStartTime())) && session.getEndTime().isAfter(session1.getEndTime()))
                        throw new Exception("Coach is occupied at that time");
                }
                if(session1.getSportsGroup().equals(session.getSportsGroup())){
                    if((session.getStartTime().isAfter(session1.getStartTime())
                            && session.getStartTime().isBefore(session1.getEndTime())) ||
                            (session.getEndTime().isAfter(session1.getStartTime()) && session.getEndTime().isBefore(session1.getEndTime()))
                            || (session.getStartTime().equals(session1.getStartTime()) && session.getEndTime().equals(session1.getEndTime()))
                            || (session.getStartTime().isBefore(session1.getStartTime())) && session.getEndTime().isAfter(session1.getEndTime()))
                        throw new Exception("Group is occupied at that time");
                }
            }
        }

        coach.getSessions().add(session);
        field.getSessions().add(session);
        session.setSportsGroup(sportsGroup);

        sessionRepo.save(session);
        return "Successfully created!";
    }
}
