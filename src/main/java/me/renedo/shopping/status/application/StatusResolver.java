package me.renedo.shopping.status.application;

import java.util.Arrays;
import java.util.List;

import me.renedo.shopping.shared.Service;
import me.renedo.shopping.status.domain.Status;

@Service
public class StatusResolver {

    public List<Status> getAllStatus(){
        return Arrays.stream(Status.ACTIVE.getDeclaringClass().getEnumConstants()).toList();
    }
}
