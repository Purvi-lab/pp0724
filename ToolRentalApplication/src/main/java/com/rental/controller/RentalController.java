package com.rental.controller;

import com.rental.model.RentalAgreement;
import com.rental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/rental")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @PostMapping("/checkout")
    public RentalAgreement checkout(@RequestParam String toolCode,
                                    @RequestParam int rentalDays,
                                    @RequestParam int discountPercent,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate) {
        return rentalService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);
    }
}
