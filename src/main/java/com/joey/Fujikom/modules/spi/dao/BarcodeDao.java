package com.joey.Fujikom.modules.spi.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.joey.Fujikom.common.persistence.BaseDao;
import com.joey.Fujikom.modules.spi.entity.Barcode;

@Repository
public class BarcodeDao  extends BaseDao<Barcode>{

	
	public List<String> getBarcodeValue(){
		return find("select barcodeValue from Barcode");
	}
	


}
