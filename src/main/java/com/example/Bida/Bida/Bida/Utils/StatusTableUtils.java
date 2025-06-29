package com.example.Bida.Bida.Bida.Utils;

import com.example.Bida.Bida.Bida.Enum.CategoryTable;
import com.example.Bida.Bida.Bida.Enum.StatusOrder;
import com.example.Bida.Bida.Bida.Enum.StatusPay;
import com.example.Bida.Bida.Bida.Enum.StatusTable;

import java.util.HashMap;
import java.util.Map;

public class StatusTableUtils {
    public static final Map<StatusTable, String> statusMap = new HashMap<>();
    public static final Map<CategoryTable, String> categoryMap = new HashMap<>();
    public static final Map<StatusOrder, String> statusOrderMap = new HashMap<>();
    public static final Map<StatusPay, String> statusPayMap = new HashMap<>();
    static {
        statusMap.put(StatusTable.AVAILABLE, "Có sẵn");
        statusMap.put(StatusTable.BOOKED, "Đã đặt");
        statusMap.put(StatusTable.INUSE, "Đang sử dụng");
    }

    static {
        categoryMap.put(CategoryTable.POOL, "Bia lỗ");
        categoryMap.put(CategoryTable.CAROM, "Bia ba băng");
    }

    static {
        statusOrderMap.put(StatusOrder.PLAYED, "Đã chơi");
        statusOrderMap.put(StatusOrder.PENDING, "Đang chờ");
        statusOrderMap.put(StatusOrder.CANCEL, "Đã hủy");
        statusOrderMap.put(StatusOrder.COMPLETED, "Đã thanh toán");
    }
    static {
        statusPayMap.put(StatusPay.PENDING, "Chờ thanh toán");
        statusPayMap.put(StatusPay.SUCCESS, "Thanh toán thành công");
        statusPayMap.put(StatusPay.FAIL, "Thanh toán thất bại");
    }

}
