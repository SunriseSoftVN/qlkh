package com.qlkh.server.rest;

import com.qlkh.core.client.model.Task;
import com.qlkh.server.dao.core.GeneralDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The Class ReportController.
 *
 * @author Nguyen Duc Dung
 * @since 4/20/14 12:09 PM
 */
@Controller
@RequestMapping("/")
public class ReportController {

    @Autowired
    private GeneralDao generalDao;

    @RequestMapping(value = "report", method = RequestMethod.GET)
    public @ResponseBody UserBean getReport() {
        generalDao.getAll(Task.class);
        return new UserBean("dung ne");
    }

}
