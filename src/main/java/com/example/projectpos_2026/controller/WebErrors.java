package com.example.projectpos_2026.controller;

import com.example.projectpos_2026.exception.BusinessException;
import org.springframework.dao.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;

@ControllerAdvice
public class WebErrors {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    public String business(BusinessException ex, Model model) { model.addAttribute("error", ex.getMessage()); return "error"; }
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    public String input(Exception ex, Model model) { model.addAttribute("error", "Enter valid values in all required fields."); return "error"; }
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.CONFLICT)
    public String database(DataAccessException ex, Model model) {
        model.addAttribute("error", "The record could not be saved. It may have changed or contain a duplicate value. Reload and try again."); return "error";
    }
    @ModelAttribute public void messages(Model model) { }
}
