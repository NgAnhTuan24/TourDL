package org.example.controller;


import org.example.dto.CalendarDTO;
import org.example.modal.Calendar;
import org.example.service.ICalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/Calendar")
public class CalendarController {
    @Autowired
    private ICalendarService calendarService;
    @GetMapping("/Calendars")
    public List<Calendar> listCalendar(){
        List<Calendar> calendars = calendarService.getAllCalendar();
        return calendars;
    }
    @PostMapping("/Calendars")
    public ResponseEntity<Calendar> createCalendar(@RequestBody Calendar calendar){
        Calendar newCalendar = calendarService.createNewCalendar(calendar);
        return new ResponseEntity<>(newCalendar, HttpStatus.CREATED);
    }
    @GetMapping("/Calendars/{tourId}")
    public List<Calendar> listCalendarByTourId(@PathVariable("tourId") String id){
        List<Calendar> calendarsbyTourId = calendarService.findCalendarbyTour(id);
        return calendarsbyTourId;
    }
    // API để lấy các calendar đã hết hạn
    @GetMapping("/Calendars/expired")
    public ResponseEntity<?> getExpiredCalendars() {
        try {
            // Gọi service để lấy các calendar hết hạn
           List<CalendarDTO> expiredCalendars = calendarService.getExpiredCalendars();
            // Trả về danh sách các calendar hết hạn nếu có
            return new ResponseEntity<>(expiredCalendars, HttpStatus.OK);
        } catch (RuntimeException rx) {
            // Trả về thông báo lỗi 404 nếu không có calendar hết hạn
            return new ResponseEntity<>(rx.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/Calendars/near-future")
    public ResponseEntity<?> getCalendarsWithinThreeDaysFromNow(){
        try{
            List<CalendarDTO> CalendarsWithinOneDays = calendarService.getCalendarsWithinThreeDaysFromNow();
            return new ResponseEntity<>(CalendarsWithinOneDays,HttpStatus.OK);
        }catch (RuntimeException re){
            return new ResponseEntity<>(re.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/Calendars/current-and-next-month")
    public ResponseEntity<?> getCalendarsInCurrentAndNextMonth(){
        try {
            List<CalendarDTO> calendarDTOS = calendarService.getCalendarsInCurrentAndNextMonth();
            return new ResponseEntity<>(calendarDTOS,HttpStatus.OK);
        }catch (RuntimeException re){
            return new ResponseEntity<>(re.getMessage(),HttpStatus.NOT_FOUND);

        }
    }


//    @GetMapping("/Calendar/StarteDates")
//    public List<Date> listDate(){
//        List<Date> startDates = calendarService.getAllStartedDate();
//        return startDates;
//    }


}
