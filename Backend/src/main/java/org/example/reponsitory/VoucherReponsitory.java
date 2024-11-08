package org.example.reponsitory;

import org.example.modal.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherReponsitory extends JpaRepository<Voucher, Integer> {

    @Query("SELECT COUNT(v) FROM Voucher v")
    Integer totalVouchers();
}
