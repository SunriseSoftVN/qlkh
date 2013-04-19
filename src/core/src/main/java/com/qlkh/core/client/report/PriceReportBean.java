package com.qlkh.core.client.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class PriceSumReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 4/16/13 7:25 PM
 */
public class PriceReportBean implements Serializable {

    private Map<String, PriceColumnBean> columns = new HashMap<String, PriceColumnBean>();
    private List<PriceReportBean> children = new ArrayList<PriceReportBean>();
    private String[] regex = new String[0];

    private String stt;
    private String code;
    private String name;
    private String unit;
    private Double price;
    private double quantity;
    private Long materialId;
    private long taskId;

    private boolean isCalculated;

    public PriceReportBean() {
    }

    public PriceReportBean(String stt, String code, String name, String... regex) {
        this.regex = regex;
        this.stt = stt;
        this.code = code;
        this.name = name;
    }

    public void calculate() {
        for (PriceReportBean child : children) {
            if (!child.isCalculated) {
                child.calculate();
            }
        }

        if (children.isEmpty()) {
            for (PriceColumnBean column : columns.values()) {
                if (column.getTaskWeight() != null && price != null) {
                    double weight = column.getTaskWeight() * quantity;
                    double price = weight * this.price;
                    column.setWeight(weight);
                    column.setPrice(price);
                }
            }
        } else {
            // Sum up same materials.
            List<PriceReportBean> removeChildren = new ArrayList<PriceReportBean>();
            for (int i = 0; i < children.size() - 1; i++) {
                PriceReportBean child1 = children.get(i);
                for (int j = i + 1; j < children.size(); j++) {
                    PriceReportBean child2 = children.get(j);
                    if (child1 != child2
                            && child1.getMaterialId() != null
                            && child1.getMaterialId().equals(child2.getMaterialId())) {
                        for (PriceColumnBean column1 : child1.getColumns().values()) {
                            PriceColumnBean column2 = child2.getColumns().get(String.valueOf(column1.getId()));
                            if (column2 != null && column2.getWeight() != null) {
                                if (column1.getWeight() == null) {
                                    column1.setWeight(column2.getWeight());
                                } else {
                                    double weight = column1.getWeight() + column2.getWeight();
                                    column1.setWeight(weight);
                                }

                                if (column1.getPrice() == null) {
                                    column1.setPrice(column2.getPrice());
                                } else {
                                    double price = column1.getPrice() + column2.getPrice();
                                    column1.setPrice(price);
                                }
                            }
                        }

                        removeChildren.add(child2);
                    }
                }
            }

            children.removeAll(removeChildren);

            //Sum all children.
            for (PriceColumnBean column : columns.values()) {
                double price = 0d;

                for (PriceReportBean child : children) {
                    PriceColumnBean childColumn = child.getColumns().get(String.valueOf(column.getId()));
                    if (childColumn != null) {
                        if (childColumn.getPrice() != null) {
                            price += childColumn.getPrice();
                        }
                    }
                }

                if (price > 0) {
                    column.setPrice(price);
                }
            }
        }

        isCalculated = true;
    }

    public Map<String, PriceColumnBean> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, PriceColumnBean> columns) {
        this.columns = columns;
    }

    public List<PriceReportBean> getChildren() {
        return children;
    }

    public void setChildren(List<PriceReportBean> children) {
        this.children = children;
    }

    public String[] getRegex() {
        return regex;
    }

    public void setRegex(String[] regex) {
        this.regex = regex;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}
