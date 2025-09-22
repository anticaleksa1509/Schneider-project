package raf.rs.SportsBooking.bootstrap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.rs.SportsBooking.model.*;
import raf.rs.SportsBooking.repository.CoachRepo;
import raf.rs.SportsBooking.repository.FieldRepo;
import raf.rs.SportsBooking.repository.SessionRepo;
import raf.rs.SportsBooking.repository.SportsGroupRepo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class BootStrapClass implements CommandLineRunner {

    @Autowired
    FieldRepo fieldRepo;
    @Autowired
    CoachRepo coachRepo;
    @Autowired
    SportsGroupRepo sportsGroupRepo;
    @Autowired
    SessionRepo sessionRepo;

    static int counter = 1;
    static char c = 'A';

    @Override
    public void run(String... args) throws Exception {

        String[] sportsFields = {
                "Football",
                "Tennis ",
                "Basketball ",
                "Volleyball ",
                "Baseball "
        };

        String[] names = {"John", "Sarah", "Michael", "Emily", "David"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones"};

        String[] areaOfExpertise = {"Sports Psychology", "Fitness Training", "Nutrition Coaching", "Strength and Conditioning", "Team Management"};
        String[] coachingTitles = {"Head Coach", "Assistant Coach", "Fitness Coach", "Technical Coach", "Defensive Coordinator"};

        String[] outOrIn = {"outside","inside"};

        String email = "john.doe@gmail.com";

        for(int i = 0; i < 5; i++){

            Random random = new Random();
            Coach coach = new Coach();
            Field field = new Field();
            SportsGroup sportsGroup = new SportsGroup();
            Session session = new Session();

            sportsGroup.setMarkGroup("Group" + (i+1));
            String[] pomocno = {"competitive", "recreational"};
            int maxMember = ThreadLocalRandom.current().nextInt(10,30);
            sportsGroup.setMaxMembers(maxMember);
            sportsGroup.setTypeOfSport(sportsFields[i]);
            sportsGroup.setType(pomocno[random.nextInt(2)]);
            sportsGroup.setSessions(new ArrayList<>());


            field.setType(sportsFields[i]);
            field.setMark(c+"Field" + (i + 1));
            c = (char) (c + 1);
            int capacity = ThreadLocalRandom.current().nextInt(10,30);
            field.setCapacity(capacity);
            field.setOutsideOrInside(outOrIn[random.nextInt(2)]);
            field.setSessions(new ArrayList<>());

            coach.setName(names[i]);
            coach.setLastName(lastNames[i]);
            coach.setAreaOfExpertise(areaOfExpertise[i]);
            coach.setCoachingTitle(coachingTitles[i]);
            coach.setSessions(new ArrayList<>());
            coach.setEmail(email + i);


            session.setCoach(coach);
            session.setField(field);
            session.setSportsGroup(sportsGroup);
            session.setReserved(false);//inicijalno ni jedna nije rezervisana
            int randomBegging = ThreadLocalRandom.current().nextInt(9,22);
            session.setStartTime(LocalTime.of(randomBegging,0));
            int randomEnding = randomBegging + 1;//session is valid for one hour
            session.setEndTime(LocalTime.of(randomEnding, 0));
            int randomDayOfWeek = random.nextInt(7) + 1;//od 1 do 7
            session.setDayOfWeek(DayOfWeek.of(randomDayOfWeek));
            String[] typeOfSessions = {"training","competition","freeTime"};
            session.setTypeOfSession(typeOfSessions[random.nextInt(3)]);


            fieldRepo.save(field);
            coachRepo.save(coach);
            sportsGroupRepo.save(sportsGroup);
            sessionRepo.save(session);
        }
    }
}
