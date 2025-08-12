package com.reliaquest.api.service;

import com.reliaquest.api.client.MockEmployeeClient;
import com.reliaquest.api.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    void deleteByIdUsesName() {
        var client = mock(MockEmployeeClient.class);
        var svc = new EmployeeService(client);

        when(client.getById("id-123")).thenReturn(
                new Employee("id-123","Bill Bob",89750,24,"Documentation Engineer","billBob@company.com")
        );
        when(client.deleteByName("Bill Bob")).thenReturn(true);

        String result = svc.deleteByIdReturnName("id-123");
        assertThat(result).isEqualTo("Bill Bob");
        verify(client).getById("id-123");
        verify(client).deleteByName("Bill Bob");
    }

    @Test
    void highestSalary_ok() {
        var client = mock(MockEmployeeClient.class);
        var svc = new EmployeeService(client);
        when(client.getAll()).thenReturn(List.of(
                new Employee("1","A",100,30,"T","a@x.com"),
                new Employee("2","B",320800,61,"T2","b@x.com")
        ));
        assertThat(svc.highestSalary()).isEqualTo(320800);
    }

    @Test
    void top10_ok() {
        var client = mock(MockEmployeeClient.class);
        var svc = new EmployeeService(client);
        when(client.getAll()).thenReturn(List.of(
                new Employee("1","X",10,20,"",""),
                new Employee("2","Y",30,20,"",""),
                new Employee("3","Z",20,20,"","")
        ));
        assertThat(svc.top10NamesBySalary()).containsExactly("Y","Z","X");
    }
}
