/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.system;

import com.qlkh.core.client.action.system.Upgrade134Action;
import com.qlkh.core.client.action.system.Upgrade134Result;
import com.qlkh.core.client.model.MaterialIn;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The Class Upgrade116Handler.
 *
 * @author Nguyen Duc Dung
 * @since 11/16/12, 2:27 PM
 */
public class Upgrade134Handler extends AbstractHandler<Upgrade134Action, Upgrade134Result> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<Upgrade134Action> getActionType() {
        return Upgrade134Action.class;
    }

    @Override
    public Upgrade134Result execute(Upgrade134Action action, ExecutionContext context) throws DispatchException {
        List<MaterialIn> materialIns = generalDao.findCriteria(MaterialIn.class, Restrictions.isNull("group"));
        for (MaterialIn materialIn : materialIns) {
            if (materialIn.getMaterialPerson() != null && materialIn.getMaterialPerson().getGroup() != null) {
                materialIn.setGroup(materialIn.getMaterialPerson().getGroup());
            }
        }
        generalDao.saveOrUpdate(materialIns);
        return new Upgrade134Result();
    }
}
