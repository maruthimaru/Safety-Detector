package com.soundary.saftyroute.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.soundary.saftyroute.db.table.User;

@Dao
public interface UserDao {

    @Insert
    public void userInser(User user);

    @Query("Select * from user where mobile_number=:mobileNumber and password=:password")
    public User getValidUser(String mobileNumber,String password);
}
