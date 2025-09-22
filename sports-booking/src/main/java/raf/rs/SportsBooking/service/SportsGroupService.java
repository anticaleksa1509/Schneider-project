package raf.rs.SportsBooking.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.rs.SportsBooking.model.SportsGroup;
import raf.rs.SportsBooking.repository.SportsGroupRepo;

import java.util.List;
import java.util.Optional;

@Service
public class SportsGroupService {

    @Autowired
    SportsGroupRepo sportsGroupRepo;

    public String createGroup(SportsGroup sportsGroup) throws Exception{
        if(sportsGroup.getMarkGroup() != null && sportsGroup.getTypeOfSport() != null &&
        sportsGroup.getMaxMembers() != 0 && sportsGroup.getType() != null){
            if(!(sportsGroup.getMaxMembers() > 10 && sportsGroup.getMaxMembers() < 30))
                throw new Exception("Number of members must be between 10 and 30");
            sportsGroupRepo.save(sportsGroup);
            return "Successfully created!";
        }
        throw new Exception("All fields must be covered!");
    }

    public List<SportsGroup> getByTypeOfSport(String typeOfSport) throws Exception{
        Optional<List<SportsGroup>> optionalSportsGroup = sportsGroupRepo.findByTypeOfSport(typeOfSport);
        if(optionalSportsGroup.isPresent())
            return optionalSportsGroup.get();
        throw new Exception("There is no sports group with that type of sport");
    }
}
