package com.demo.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demo.entities.Item;

@Service
public class CartServiceImpl implements CartService{

	@Override
	public double total(List<Item> items) {
		double s = 0;
		for (Item item : items) {
			s += item.getProduct().getPrice() * item.getQuantity();
		}
		return s;
	}

	@Override
	public int exist(int productId, List<Item> items) {
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getProduct().getId() == productId) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public List<List<String>> uploadExcel(MultipartFile excelFile) {
		   List<List<String>> excelData = new ArrayList<>();

	        try {
	            InputStream inputStream = excelFile.getInputStream(); // Lấy luồng dữ liệu từ file Excel
	            Workbook workbook = new XSSFWorkbook(inputStream); // Tạo một workbook từ luồng dữ liệu
	            Sheet sheet = workbook.getSheetAt(0); // Lấy ra sheet đầu tiên
	            Iterator<Row> iterator = sheet.iterator(); // Lặp qua các dòng trong sheet

	            while (iterator.hasNext()) { // Duyệt qua từng dòng
	                Row currentRow = iterator.next(); // Lấy dòng hiện tại
	                Iterator<Cell> cellIterator = currentRow.iterator(); // Lặp qua các ô trong dòng
	                List<String> rowData = new ArrayList<>(); // Tạo danh sách để lưu dữ liệu của dòng hiện tại

	                while (cellIterator.hasNext()) { // Duyệt qua từng ô trong dòng
	                    Cell currentCell = cellIterator.next(); // Lấy ô hiện tại
	                    if (currentCell.getCellType() == CellType.STRING) { // Nếu ô là kiểu String
	                        rowData.add(currentCell.getStringCellValue()); // Thêm giá trị của ô vào danh sách dữ liệu của dòng
	                    } else if (currentCell.getCellType() == CellType.NUMERIC) { // Nếu ô là kiểu Numeric
	                        rowData.add(String.valueOf(currentCell.getNumericCellValue())); // Thêm giá trị của ô vào danh sách dữ liệu của dòng
	                    }
	                }
	                excelData.add(rowData); // Sau khi duyệt qua tất cả các ô trong dòng, thêm danh sách dữ liệu của dòng vào danh sách dữ liệu của bảng Excel
	            }
	            workbook.close(); // Đóng workbook sau khi đã đọc xong dữ liệu
	        } catch (IOException e) { // Bắt các ngoại lệ nếu có lỗi xảy ra trong quá trình đọc file
	            e.printStackTrace();
	        }
	        System.out.println(excelData);
	        return excelData;
	}

}
