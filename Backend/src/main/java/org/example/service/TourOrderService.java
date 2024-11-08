package org.example.service;

import com.mysql.cj.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.dto.TourOrderDTO;
import org.example.modal.Tour;
import org.example.modal.TourOrder;
import org.example.reponsitory.TourOrderReponsitory;
import org.example.reponsitory.TourReponsitory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TourOrderService {
    @Autowired
    private TourOrderReponsitory tourOrderReponsitory;
    @Autowired
    private TourReponsitory tourReponsitory;


    public List<TourOrderDTO> getAllTourOrder() {
        List<TourOrderDTO> tourOrderDTOs = new ArrayList<>();

        // Lấy tất cả các TourOrder từ repository và chuyển đổi sang TourOrderDTO
        for (TourOrder tourOrder : tourOrderReponsitory.findAll()) {
            TourOrderDTO tourOrderDTO = new TourOrderDTO(tourOrder);
            tourOrderDTOs.add(tourOrderDTO);
        }
        // Trả về danh sách TourOrderDTO đã cập nhật
        return tourOrderDTOs;
    }

    // lay doanh thu
    public double getRevenue(String type) {
        Timestamp startDate;
        Timestamp endDate;

        // Tính toán phạm vi ngày, tháng, năm tùy theo type
        String timeType = type.toUpperCase();
        switch (timeType) {
            case "DAY":
                // Chuyển LocalDate thành LocalDateTime và sau đó thành Timestamp
                startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay()); // Tính từ 00:00:00 của ngày hiện tại
                endDate = Timestamp.valueOf(LocalDate.now().atTime(23, 59, 59)); // Tính đến 23:59:59 của ngày hiện tại
                return tourOrderReponsitory.totalRevenueByDay(startDate, endDate);

            case "MONTH":
                // Tính từ ngày đầu tháng đến ngày cuối tháng
                startDate = Timestamp.valueOf(LocalDate.now().withDayOfMonth(1).atStartOfDay()); // Ngày đầu tháng
                endDate = Timestamp.valueOf(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59)); // Ngày cuối tháng
                return tourOrderReponsitory.totalRevenueByMonth(startDate, endDate);

            case "YEAR":
                // Tính từ ngày đầu năm đến ngày cuối năm
                startDate = Timestamp.valueOf(LocalDate.now().withDayOfYear(1).atStartOfDay()); // Ngày đầu năm
                endDate = Timestamp.valueOf(LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear()).atTime(23, 59, 59)); // Ngày cuối năm
                return tourOrderReponsitory.totalRevenueByYear(startDate, endDate);

            default:
                // Trường hợp mặc định, tính doanh thu trong ngày
                startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
                endDate = Timestamp.valueOf(LocalDate.now().atTime(23, 59, 59));
                return tourOrderReponsitory.totalRevenueByDay(startDate, endDate);
        }
    }

    // get top tour
    public List<TourOrderDTO> listTopTours() {
        Pageable pageable = PageRequest.of(0, 10); // Sử dụng PageRequest của Spring
        return tourOrderReponsitory.getTopTourOrders(pageable);
    }

    // tour order by month

    public int countTourOrderByMonth(){
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().withDayOfMonth(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59));
        return tourOrderReponsitory.countTourOrderByMonth(startDate, endDate);
    }

    // tao 1 tour order
    @Transactional
    public void createNewTourOrder(TourOrder tourOrder, String tourId) {
        Optional<Tour> optionalTour = tourReponsitory.findById(tourId); // Giả sử repository của Tour là `tourRepository`

        if (optionalTour.isPresent()) {
            Tour tour = optionalTour.get();
            tourOrder.setTour(tour); // Giả sử `TourOrder` có phương thức `setTour`
            tourOrderReponsitory.save(tourOrder);
        } else {
            throw new EntityNotFoundException("Tour with ID " + tourId + " not found.");
        }
    }
}

