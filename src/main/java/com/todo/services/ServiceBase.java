/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.services;

/**
 *
 * @author Safarifone
 */
public class ServiceBase {
    
    CbsAgent ca = null;
    public void init(){
        ca = new CbsAgent();
    }
    public String process(String si){
        return "";
    }
    public void close(){
        
    }
}
