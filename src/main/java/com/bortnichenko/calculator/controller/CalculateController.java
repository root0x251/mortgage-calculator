package com.bortnichenko.calculator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Vyacheslav Alekseevich
 * @date 2020-07-22
 */

@Controller
public class CalculateController {

    private int payment;

    @GetMapping("/")
    public String initDefaultPara(@RequestParam(name = "cost", defaultValue = "5000000") String cost,
                                  @RequestParam(name = "payment", defaultValue = "1500000") String payment,
                                  @RequestParam(name = "creditYears", defaultValue = "5") String creditYears,
                                  @RequestParam(name = "rate", defaultValue = "9.5") String rate,
                                  @RequestParam(name = "creditCount", defaultValue = "0") String creditCount,
                                  @RequestParam(name = "paymentResult", defaultValue = "0") String paymentResult,
                                  @RequestParam(name = "overpayment", defaultValue = "0") String overpayment,
                                  Model model) {

        try {

            // стоимость
            int costLastValue = Integer.parseInt(cost);
            // первоначальный взнос
            int paymentLastValue = Integer.parseInt(payment);
            // количество лет
            int creditYearsLastValue = Integer.parseInt(creditYears);
            // процентная ставка
            float rateLastValue = Float.parseFloat(rate);

            if (costLastValue >= paymentLastValue) {

                // ежемесячная ставка
                double monthlyRate = rateLastValue / 12 / 100;
                // общая ставка
                double totalBet = Math.pow(monthlyRate + 1, creditYearsLastValue * 12);
                // ежемесячный платеж
                double monthlyPayment = (costLastValue - paymentLastValue) * monthlyRate * totalBet / (totalBet - 1);

                model.addAttribute("cost", costLastValue);
                model.addAttribute("payment", paymentLastValue);
                model.addAttribute("creditYears", creditYearsLastValue);
                model.addAttribute("rate", rateLastValue);

                model.addAttribute("creditCount", costLastValue - paymentLastValue);
                model.addAttribute("paymentResult", String.format("%.0f", monthlyPayment));

                model.addAttribute("overpayment", String.format("%.0f", monthlyPayment * (creditYearsLastValue * 12) - (costLastValue - paymentLastValue)));
            } else {
                model.addAttribute("cost", cost);
                model.addAttribute("payment", payment);
                model.addAttribute("creditYears", creditYears);
                model.addAttribute("rate", rate);
            }

        } catch (NumberFormatException e) {

            model.addAttribute("cost", cost);
            model.addAttribute("payment", payment);
            model.addAttribute("creditYears", creditYears);
            model.addAttribute("rate", rate);

            return "index";
        }

        return "index";
    }

    @PostMapping("/")
    public String setParam(@RequestParam(name = "cost", defaultValue = "5000000") String cost,
                           @RequestParam(name = "payment", defaultValue = "1500000") String payment,
                           @RequestParam(name = "creditYears", defaultValue = "5") String creditYears,
                           @RequestParam(name = "rate", defaultValue = "9.5") String rate, Model model) {

        model.addAttribute("cost", cost);
        model.addAttribute("payment", payment);
        model.addAttribute("creditYears", creditYears);
        model.addAttribute("rate", rate);


        return "redirect:/?cost=" + cost + "&payment=" + payment + "&creditYears=" + creditYears + "&rate=" + rate;
    }
}
