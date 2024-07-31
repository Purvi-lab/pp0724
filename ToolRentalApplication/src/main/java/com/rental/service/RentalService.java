package com.rental.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.model.RentalAgreement;
import com.rental.model.Tool;
import com.rental.model.ToolType;
import com.rental.repository.ToolRepository;

@Service
public class RentalService {
    @Autowired
    private ToolRepository toolRepository;

    public RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        Tool tool = toolRepository.findByToolCode(toolCode);
        if (tool == null) {
            throw new IllegalArgumentException("Tool not found for the provided tool code.");
        }

        ToolType toolType = tool.getToolType();
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        double dailyCharge = toolType.getDailyCharge();

        int chargeDays = calculateChargeDays(toolType, checkoutDate, dueDate);
        double preDiscountCharge = chargeDays * dailyCharge;
        double discountAmount = preDiscountCharge * discountPercent / 100;
        double finalCharge = preDiscountCharge - discountAmount;

        return new RentalAgreement(
                tool.getToolCode(),
                toolType.getName(),
                tool.getBrand(),
                rentalDays,
                checkoutDate,
                dueDate,
                dailyCharge,
                chargeDays,
                preDiscountCharge,
                discountPercent,
                discountAmount,
                finalCharge
        );
    }

    private int calculateChargeDays(ToolType toolType, LocalDate checkoutDate, LocalDate dueDate) {
        int chargeDays = 0;
        for (LocalDate date = checkoutDate.plusDays(1); !date.isAfter(dueDate); date = date.plusDays(1)) {
            boolean isChargeable = isChargeableDay(toolType, date);
            System.out.println("Date: " + date + ", Chargeable: " + isChargeable); // Debug statement
            if (isChargeable) {
                chargeDays++;
            }
        }
        return chargeDays;
    }

    private boolean isChargeableDay(ToolType toolType, LocalDate date) {
        if (isHoliday(date)) {
            return toolType.isHolidayCharge();
        }
        if (isWeekend(date)) {
            return toolType.isWeekendCharge();
        }
        return toolType.isWeekdayCharge();
    }

    private boolean isHoliday(LocalDate date) {
        // Independence Day
        if (date.getMonth() == Month.JULY) {
            if ((date.getDayOfMonth() == 4 && !isWeekend(date))|| 
                (date.getDayOfMonth() == 3 && date.getDayOfWeek() == DayOfWeek.FRIDAY) ||
                (date.getDayOfMonth() == 5 && date.getDayOfWeek() == DayOfWeek.MONDAY)) {
                return true;
            }
        }
        // Labor Day
        if (date.getMonth() == Month.SEPTEMBER && date.getDayOfWeek() == DayOfWeek.MONDAY && date.getDayOfMonth() <= 7) {
            return true;
        }
        return false;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
