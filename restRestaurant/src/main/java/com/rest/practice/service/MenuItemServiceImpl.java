package com.rest.practice.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import ch.qos.logback.classic.util.StatusViaSLF4JLoggerFactory;
import com.rest.practice.Exception.CantAccessDatabseException;
import com.rest.practice.Exception.InternalServerErrorException;
import com.rest.practice.Exception.MenuItemNotFoundException;
import com.rest.practice.Exception.MenuNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.rest.practice.models.Menu;
import com.rest.practice.models.MenuItem;
import com.rest.practice.repository.MenuItemRepository;
import com.rest.practice.repository.MenuRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuItemServiceImpl implements MenuItemService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	public MenuItem save(MenuItem menuItem) throws InternalServerErrorException {
		try {
			menuItemRepository.save(menuItem);
		} catch (Exception e) {
			throw new InternalServerErrorException("Something went wrong when saving");
		}
		return menuItem;
	}
	
	@Override
	public List<MenuItem> findAll() {
		return menuItemRepository.findAll();
	}

	@Override
	public MenuItem edit(Long id, MenuItem updatedMenuItem) throws MenuItemNotFoundException, InternalServerErrorException, BeansException {

		logger.debug("edit menuItem");
		MenuItem menuItem = this.find(id);
		BeanUtils.copyProperties(updatedMenuItem, menuItem);
		menuItemRepository.save(menuItem);
		return menuItem;
	}

	@Override
	public void delete(Long id) throws MenuItemNotFoundException {
		try {
			menuItemRepository.deleteById(id);
		} catch (IllegalArgumentException e) {
			throw new MenuItemNotFoundException("Could not delete menuItem with id: " + id.toString());
		}
	}

	@Override
	public MenuItem find(Long id) throws MenuItemNotFoundException{
		return menuItemRepository.findById(id).orElseThrow(
				() -> new MenuItemNotFoundException("menu item with id: " + id + " was not found"));

	}


	@Override
	public void addToMenu(Long itemId, Long menuId) throws MenuItemNotFoundException, MenuNotFoundException {
		MenuItem menuItem = this.find(itemId);
		Menu menu = menuService.find(menuId);
		menu.getMenuitems().add(menuItem);
		menuRepository.save(menu);
	}
} 
 