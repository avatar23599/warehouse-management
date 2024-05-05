package com.demo.configurations;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.Source;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.demo.dtos.InputDTO;
import com.demo.dtos.InputInfoDTO;
import com.demo.entities.Account;
import com.demo.entities.Input;
import com.demo.entities.Inputifo;
import com.demo.entities.Product;
import com.demo.entities.Suplier;
import com.demo.services.ProductService;



@Configuration
public class ModelMapperConfiguration {
	@Autowired
	private Environment environment;
	
	@Autowired
	private ProductService productService;

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper mapper = new ModelMapper();

		// Convert

		Converter<Integer, Suplier> convertIntgerToSuplier = new AbstractConverter<Integer, Suplier>() {

			@Override
			protected Suplier convert(Integer source) {
				return new Suplier(source);
			}
		};
		
		Converter<Integer, Product> convertIntgerToProduct = new AbstractConverter<Integer, Product>() {

			@Override
			protected Product convert(Integer source) {
				return productService.find(source);
			}
		};
		

		
		Converter<Integer, Account> convertIntgerToAccount = new AbstractConverter<Integer, Account>() {

			@Override
			protected Account convert(Integer source) {
				return new Account(source);
			}
		};
		Converter<Double, Float> convertDoubleToFloat = new AbstractConverter<Double, Float>() {

			@Override
			protected Float convert(Double source) {
				return Float.parseFloat(source.toString());
			}
		};
		

		mapper.typeMap(InputDTO.class, Input.class).addMappings(m -> {
			m.using(convertIntgerToSuplier).map(InputDTO::getSuplierId, Input::setSuplier);
			m.using(convertIntgerToAccount).map(InputDTO::getAccountId, Input::setAccount);
		});
		
		mapper.typeMap(Inputifo.class, InputInfoDTO.class).addMappings(m -> {
			m.using(convertIntgerToProduct).map(Inputifo::getProductId, InputInfoDTO::setProduct);
		});
		
		mapper.addMappings(new PropertyMap<Input, InputDTO>() {

			@Override
			protected void configure() {
				map().setId(source.getId());
				map().setAccountId(source.getAccount().getId());
				map().setDate(source.getDate());
				map().setInputinfo(source.getInputinfo());
				map().setStatus(source.getStatus());
				map().setSuplierId(source.getSuplier().getId());
			}
		});
		
		mapper.addMappings(new PropertyMap<Inputifo, InputInfoDTO>() {

			@Override
			protected void configure() {
				map().setId(source.getId());
				map().setCount(source.getCount());
				map().setInput(source.getInput());
			}
		});
		
		mapper.addMappings(new PropertyMap<InputInfoDTO, Inputifo>() {

			@Override
			protected void configure() {
				map().setId(source.getId());
				map().setCount(source.getCount());
				map().setInput(source.getInput());
				map().setProductId(source.getProduct().getId());
			}
		});
		
		mapper.addMappings(new PropertyMap<InputDTO, Input>() {

			@Override
			protected void configure() {
				map().setId(source.getId());
				map().setDate(source.getDate());
				map().setInputinfo(source.getInputinfo());
				map().setStatus(source.getStatus());
			}
		});
		return mapper;

	}
}
