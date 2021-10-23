package com.ecommerce.inventorymanagement.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.ecommerce.inventorymanagement.constants.Constants;

@Component
public class ConfigMgrUtil {

	@Autowired
	ResourcePatternResolver resourceResolver;

	private static final Logger LOG = Logger.getLogger(ConfigMgrUtil.class.getName());
	private Properties configs;

	@PostConstruct
	public void init() {
		configs = new Properties();
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		Resource[] resources = null;
		try {
			resources = resourceResolver.getResources(Constants.CONFIG_FILE_PATTERN);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Error reading config files : ", e.getMessage());
		}
		if (null != resources) {
			for (Resource resource : resources) {
				yaml.setResources(resource);
				configs.putAll(yaml.getObject());
			}
		}
	}

	public String getPropertyValueAsString(String key) {
		Object value = getPropertyValue(key);
		if (null != value) {
			return value.toString();
		}
		return Constants.EMPTY;
	}

	public int getPropertyValueAsInt(String key) {
		Object value = getPropertyValue(key);
		if (null != value) {
			try {
				return Integer.parseInt(value.toString());
			} catch (NumberFormatException e) {
				LOG.log(Level.SEVERE, "Value for " + key + " is not an integer");
			}
		}
		return 0;
	}

	public long getPropertyValueAsLong(String key) {
		Object value = getPropertyValue(key);
		if (null != value) {
			try {
				return Long.parseLong(value.toString());
			} catch (NumberFormatException e) {
				LOG.log(Level.SEVERE, "Value for key -> " + key + " is not an integer");
			}
		}
		return 0;
	}

	public Object getPropertyValue(String key) {
		if (null == configs) {
			LOG.log(Level.INFO, "Config file not loaded");
		} else {
			if (key.isEmpty()) {
				LOG.log(Level.INFO, "Key is empty");
			} else if (!configs.containsKey(key)) {
				LOG.log(Level.INFO, key + " property not found in config file");
			} else {
				return configs.get(key);
			}
		}
		return null;
	}
}
