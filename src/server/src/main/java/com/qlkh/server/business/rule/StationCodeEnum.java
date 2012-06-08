/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlkh.server.business.rule;

/**
 * The Class StationCodeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 5/30/12, 3:23 PM
 */
public enum StationCodeEnum {

    COMPANY(27l),
    CAUGIAT(32l),
    TN_FOR_REPORT(999998l),
    ND_FOR_REPORT(999999l);

    private long id;

    StationCodeEnum(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
