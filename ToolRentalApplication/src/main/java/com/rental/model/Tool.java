package com.rental.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Tool {
    @Id
    private String toolCode;

    @Enumerated(EnumType.STRING)
    private ToolType toolType;

    private String brand;

    public Tool() {}

    public Tool(String toolCode, ToolType toolType, String brand) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
