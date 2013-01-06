/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2012 Mark Morgan.
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
 * 
 * Contributors:
 *     Mark Morgan - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package org.morganm.homespawnplus.storage.yaml.serialize;

import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;
import org.morganm.homespawnplus.entity.PlayerLastLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author morganm
 *
 */
@SerializableAs("PlayerLastLocation")
public class SerializablePlayerLastLocation extends AbstractSerializableEntityWithLocation<PlayerLastLocation>
implements SerializableYamlObject<PlayerLastLocation>
{
    private final static Logger log = LoggerFactory.getLogger(SerializablePlayerLastLocation.class);
	private final static String ATTR_PLAYER_NAME = "player_name";
	
	public SerializablePlayerLastLocation(PlayerLastLocation playerLastLocation) {
		super(playerLastLocation);
	}

	public SerializablePlayerLastLocation(Map<String, Object> map) {
		super(map);
		
		log.debug("SerializablePlayerLastLocation constructor, map={}",map);

		Object o = map.get(ATTR_PLAYER_NAME);
		if( o instanceof String )
			getObject().setPlayerName((String) o);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serialize();
		map.put(ATTR_PLAYER_NAME, getObject().getPlayerName());
		return map;
	}

	@Override
	protected PlayerLastLocation newEntity() {
		return new PlayerLastLocation();
	}

}