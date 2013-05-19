/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2013 Andune (andune.alleria@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
/**
 * 
 */
package com.andune.minecraft.hsp;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.reflections.Reflections;

import com.andune.minecraft.commonlib.Initializable;
import com.andune.minecraft.hsp.config.ConfigBase;
import com.andune.minecraft.hsp.config.ConfigLoader;
import com.andune.minecraft.hsp.strategy.StrategyConfig;
import com.google.inject.Injector;

/** This class is used to initialize all classes within HSP that
 * implement the Initializable interface, in order of priority.
 * 
 * @author andune
 *
 */
@Singleton
public class Initializer extends com.andune.minecraft.commonlib.Initializer {
    private final ConfigLoader configLoader;
    private final StrategyConfig strategyConfig;
    
    @Inject
    public Initializer(Reflections reflections, Injector injector,
            ConfigLoader configLoader, StrategyConfig strategyConfig) {
        super(reflections, injector);
        this.configLoader = configLoader;
        this.strategyConfig = strategyConfig;
    }

    /**
     * Called to initialize Config objects only, useful for reloading
     * configuration files.
     * 
     * @throws Exception
     */
    public void initConfigs() throws Exception {
        configLoader.flush();

        for(Initializable init : getSortedInitObjects()) {
            if( init instanceof ConfigBase )
                init.init();
        }
        
        // re-initialize strategyConfig since it preprocesses and caches event config data
        strategyConfig.init();
    }
}