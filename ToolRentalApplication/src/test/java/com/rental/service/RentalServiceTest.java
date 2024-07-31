package com.rental.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rental.model.RentalAgreement;
import com.rental.model.Tool;
import com.rental.model.ToolType;
import com.rental.repository.ToolRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RentalServiceTest {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private ToolRepository toolRepository;

    @BeforeEach
    public void setup() {
        // Ensure the tools are present in the database for each test
        toolRepository.save(new Tool("JAKR", ToolType.JACKHAMMER, "Ridgid"));
        toolRepository.save(new Tool("LADW", ToolType.LADDER, "Werner"));
        toolRepository.save(new Tool("CHNS", ToolType.CHAINSAW, "Stihl"));
        toolRepository.save(new Tool("JAKD", ToolType.JACKHAMMER, "DeWalt"));
    }

    @Test
    public void testCheckout_InvalidDiscount() {
        assertThrows(IllegalArgumentException.class, () -> {
            rentalService.checkout("JAKR", 5, 101, LocalDate.of(2015, 9, 3));
        });
    }

    @Test
    public void testCheckout_Test2() {
        RentalAgreement agreement = rentalService.checkout("LADW", 3, 10, LocalDate.of(2020, 7, 2));
        assertNotNull(agreement);
        assertEquals("LADW",  agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 5), agreement.getDueDate());
        assertEquals(1.99, agreement.getDailyRentalCharge(), 0.01);
        assertEquals(2, agreement.getChargeDays());
        assertEquals(3.98, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.40, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.58, agreement.getFinalCharge(), 0.01);
        agreement.printAgreement();
    }

    @Test
    public void testCheckout_Test3() {
        RentalAgreement agreement = rentalService.checkout("CHNS", 5, 25, LocalDate.of(2015, 7, 2));
        assertNotNull(agreement);
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals("Chainsaw", agreement.getToolType());
        assertEquals("Stihl", agreement.getToolBrand());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 7), agreement.getDueDate());
        assertEquals(1.49, agreement.getDailyRentalCharge(), 0.01);
        System.out.println("Charge Days: " + agreement.getChargeDays()); // Debug statement
        assertEquals(3, agreement.getChargeDays()); // 4th of July is a free day
        assertEquals(4.47, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.12, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.35, agreement.getFinalCharge(), 0.01);
        agreement.printAgreement();
    }

    @Test
    public void testCheckout_Test4() {
        RentalAgreement agreement = rentalService.checkout("JAKD", 6, 0, LocalDate.of(2015, 9, 3));
        assertNotNull(agreement);
        assertEquals("JAKD", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("DeWalt", agreement.getToolBrand());
        assertEquals(6, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 9, 9), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge(), 0.01);
        assertEquals(3, agreement.getChargeDays()); // Labor Day is a free day
        assertEquals(8.97, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(8.97, agreement.getFinalCharge(), 0.01);
        agreement.printAgreement();
    }

    @Test
    public void testCheckout_Test5() {
        RentalAgreement agreement = rentalService.checkout("JAKR", 9, 0, LocalDate.of(2015, 7, 2));
        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());
        assertEquals(9, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 11), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge(), 0.01);
        assertEquals(5, agreement.getChargeDays());
        assertEquals(14.95, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(14.95, agreement.getFinalCharge(), 0.01);
        agreement.printAgreement();
    }

    @Test
    public void testCheckout_Test6() {
        RentalAgreement agreement = rentalService.checkout("JAKR", 4, 50, LocalDate.of(2020, 7, 2));
        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());
        assertEquals(4, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 6), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge(), 0.01);
        assertEquals(1, agreement.getChargeDays());
        assertEquals(2.99, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.49, agreement.getDiscountAmount(), 0.01);
        assertEquals(1.49, agreement.getFinalCharge(), 0.01);
        agreement.printAgreement();
    }
}
