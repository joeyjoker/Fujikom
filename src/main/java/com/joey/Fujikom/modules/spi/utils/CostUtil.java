package com.joey.Fujikom.modules.spi.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joey.Fujikom.modules.spi.dao.ExpenseDao;
import com.joey.Fujikom.modules.spi.entity.Expense;
import com.joey.Fujikom.modules.spi.response.CostResponseData;

public class CostUtil {
	
	
	 ExpenseDao expenseDao = new ExpenseDao();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  CostResponseData deliveryCal(List<String> sizes,List<Expense> expenses){
		
		CostResponseData data= new CostResponseData();
		String cost= "";
		String ordersSize="";
		Map deliveryExpense = new HashMap();
		/*DetachedCriteria dc = expenseDao.createDetachedCriteria();
		List<Expense> expenses = expenseDao.find(dc);*/
		expenses.stream().forEach(p -> {
			deliveryExpense.put(p.getExpenseSize(),p.getExpenseDelivery());
			 
		});
		
		long num = sizes.stream().distinct().count();
		//size相同
		if (num ==1) {
			switch (sizes.get(0)) {
			case "S":
				
				if (sizes.size()>4) {
				  	cost =  "false";
				}else if (sizes.size()==1) {
					cost =  deliveryExpense.get("S").toString();
					ordersSize="S";
				}else {
					cost = deliveryExpense.get("M").toString();
					ordersSize="M";
				}
				break;
			case "M":
				switch (sizes.size()) {
				case 1:
					cost = deliveryExpense.get("M").toString();
					ordersSize="M";
					break;

				case 2:
					cost = deliveryExpense.get("LL").toString();
					ordersSize="LL";
					break;
				case 3:	
					cost = deliveryExpense.get("3L").toString();
					ordersSize="3L";
				default:
					cost = "false";
					break;
				}
				break;
			case "L":
				switch (sizes.size()) {
				case 1:
					cost = deliveryExpense.get("L").toString();
					ordersSize="L";
					break;

				case 2:
					cost = deliveryExpense.get("3L").toString();
					ordersSize="3L";
					break;
				default:
					cost = "false";
					break;
				}
			case "LL":
				if (sizes.size()==1) {
					cost = deliveryExpense.get("LL").toString();
					ordersSize="LL";
				}else {
					cost = "false";
				}
				break;
			case "3L":
				if (sizes.size()==1) {
					cost = deliveryExpense.get("3L").toString();
					ordersSize="3L";
				}else {
					cost = "false";
				}
				break;
			default:
				cost = "false";
				break;
			}
		}else {
			List<Integer> list = new ArrayList<>();
		    sizes.stream().forEach(s ->{
				switch (s) {
				case "S":
					list.add(1);
					break;
				case "M":
					list.add(2);
					break;
				case "L":
					list.add(3);
					break;
				case "LL":
					list.add(4);
					break;
				case "3L":
					list.add(5);
					break;
				}
			});
		    int sum = 0;
		    for (Integer i : list) {
		    	sum=sum+i;
		    }
		    switch (sum) {
			case 1:
				cost =  deliveryExpense.get("S").toString();
				ordersSize="S";
				break;

            case 2:
            	cost =  deliveryExpense.get("M").toString();
            	ordersSize="M";
				break;
            case 3:
	
            	cost =  deliveryExpense.get("L").toString();
            	ordersSize="L";
	            break;
            case 4:
	
            	cost =  deliveryExpense.get("LL").toString();
            	ordersSize="LL";
	            break;
            case 5:
            	cost =  deliveryExpense.get("3L").toString();
            	ordersSize="3L";
	            break;
			default:
				cost = "false";
				break;
			}
		}
		data.setOrdersSize(ordersSize);
		data.setCost(cost);
		return data;
	}

}
