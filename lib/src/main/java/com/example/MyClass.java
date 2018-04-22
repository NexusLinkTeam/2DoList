package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1,"com.jacob.www.a2dolist");
        addFriendVerify(schema);
        addTask(schema);
        addDate(schema);
        new DaoGenerator().generateAll(schema,"app/src/main/java-gen");
    }
    public static void addFriendVerify(Schema schema){
        Entity friendVerify = schema.addEntity("FriendVerify");
        friendVerify.addStringProperty("avatar");
        friendVerify.addStringProperty("nickName");
        friendVerify.addStringProperty("hello");
        friendVerify.addStringProperty("userName");
        friendVerify.addIdProperty();
    }

    public static void addTask(Schema schema){
        Entity task = schema.addEntity("Task");
        task.addIdProperty().primaryKey();
        task.addStringProperty("title");
        task.addStringProperty("content");
        task.addStringProperty("startTime");
        task.addStringProperty("endTime");
        task.addBooleanProperty("isTomato");
        task.addBooleanProperty("isChecked");
        task.addBooleanProperty("isLeader");
    }
    public static void addDate(Schema schema){
        Entity date = schema.addEntity("Date");
        date.addIdProperty();
        date.addStringProperty("date");
    }
}
