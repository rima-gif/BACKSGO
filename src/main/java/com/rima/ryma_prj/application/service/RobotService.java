package com.rima.ryma_prj.application.service;

import com.rima.ryma_prj.domain.model.Robot;
import com.rima.ryma_prj.domain.repository.RobotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RobotService {
    @Autowired
    private RobotRepository robotRepository;

    public List<Robot> getAllRobts(){
        return robotRepository.findAll();    }
    public Optional<Robot> getRobotById(Long id){
        return robotRepository.findById(id);
    }
    public Robot addRobot (Robot robot){
        return robotRepository.save(robot);
    }
    public Robot updateRobot(Long id,Robot robotDetails){
        return robotRepository.findById(id).map(robot -> {
            robot.setName(robotDetails.getName());
            robot.setStatus(robotDetails.getStatus());
            return robotRepository.save(robot);
        }).orElseThrow(()-> new RuntimeException("Robot non trouvé"));
    }
    public void delete(Long id ){
        robotRepository.deleteById(id);
    }
}
