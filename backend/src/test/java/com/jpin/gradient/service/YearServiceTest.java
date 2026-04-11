package com.jpin.gradient.service;


import com.jpin.gradient.repository.YearRepository;
import com.jpin.gradient.service.impl.YearServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class YearServiceTest {
    @Mock
    private YearRepository yearRepository;

    @InjectMocks
    private YearServiceImpl yearService;
}
