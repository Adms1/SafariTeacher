package com.adms.safariteacher.Utility;

/**
 * Created by admsandroid on 3/20/2018.
 */

public class AppConfiguration {
    public static final String BASEURL = "http://192.168.1.20:8088/WebService.asmx/";// use for office
//  public static final String BASEURL = ""; // use for client


    public static String coachId;
    public static String DateStr;
    public static String TimeStr;

}

//{
//  "Success": "True",
//  "Data": [
//    {
//      "Family_ID": "2",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "28/03/2018 16:28:56",
//      "Status_ID": "1",
//      "Contact_ID": "29",
//      "FamilyContact_ID": "1",
//      "ContactTypeName": "Self",
//      "FirstName": "sagar",
//      "LastName": "gajjar",
//      "EmailAddress": "sagar@gajjar111.com",
//      "Gender_ID": "1",
//      "DateofBirth": "01/01/1990 00:00:00",
//      "ContactPhoneNumber": "9876543210",
//      "FamilyContactCreateDate": "28/03/2018 16:28:56",
//      "FamilyContact": [
//        {
//          "Contact_ID": "35",
//          "FamilyContact_ID": "7",
//          "ContactTypeName": "Child",
//          "FirstName": "sagar1",
//          "LastName": "gajjar1",
//          "EmailAddress": "sagar1@gajjar1.com",
//          "Gender_ID": "1",
//          "DateofBirth": "01/01/1990 00:00:00",
//          "ContactPhoneNumber": "9876543210",
//          "FamilyContactCreateDate": "29/03/2018 10:01:52"
//        },
//        {
//          "Contact_ID": "36",
//          "FamilyContact_ID": "8",
//          "ContactTypeName": "Child",
//          "FirstName": "sagar2",
//          "LastName": "gajjar2",
//          "EmailAddress": "sagar2@gajjar2.com",
//          "Gender_ID": "2",
//          "DateofBirth": "01/01/1991 00:00:00",
//          "ContactPhoneNumber": "9879879870",
//          "FamilyContactCreateDate": "29/03/2018 10:36:25"
//        }
//      ]
//    },
//    {
//      "Family_ID": "3",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "28/03/2018 16:51:55",
//      "Status_ID": "1",
//      "Contact_ID": "30",
//      "FamilyContact_ID": "2",
//      "ContactTypeName": "Self",
//      "FirstName": "asd",
//      "LastName": "asd",
//      "EmailAddress": "asd@asd.asd",
//      "Gender_ID": "1",
//      "DateofBirth": "01/01/1990 00:00:00",
//      "ContactPhoneNumber": "9876546546",
//      "FamilyContactCreateDate": "28/03/2018 16:51:55",
//      "FamilyContact": []
//    },
//    {
//      "Family_ID": "4",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "28/03/2018 17:09:53",
//      "Status_ID": "1",
//      "Contact_ID": "31",
//      "FamilyContact_ID": "3",
//      "ContactTypeName": "Self",
//      "FirstName": "family",
//      "LastName": "abc",
//      "EmailAddress": "fabc@mail.com",
//      "Gender_ID": "2",
//      "DateofBirth": "28/03/2011 00:00:00",
//      "ContactPhoneNumber": "1234567890",
//      "FamilyContactCreateDate": "28/03/2018 17:09:53",
//      "FamilyContact": []
//    },
//    {
//      "Family_ID": "5",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "28/03/2018 17:17:07",
//      "Status_ID": "1",
//      "Contact_ID": "32",
//      "FamilyContact_ID": "4",
//      "ContactTypeName": "Self",
//      "FirstName": "test family",
//      "LastName": "test",
//      "EmailAddress": "testfamily@gmail.com",
//      "Gender_ID": "2",
//      "DateofBirth": "28/03/2018 00:00:00",
//      "ContactPhoneNumber": "2580963741",
//      "FamilyContactCreateDate": "28/03/2018 17:17:07",
//      "FamilyContact": []
//    },
//    {
//      "Family_ID": "6",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "28/03/2018 17:21:16",
//      "Status_ID": "1",
//      "Contact_ID": "33",
//      "FamilyContact_ID": "5",
//      "ContactTypeName": "Self",
//      "FirstName": "first child",
//      "LastName": "first",
//      "EmailAddress": "child@gmail.com",
//      "Gender_ID": "1",
//      "DateofBirth": "28/03/2018 00:00:00",
//      "ContactPhoneNumber": "2589630741",
//      "FamilyContactCreateDate": "28/03/2018 17:21:16",
//      "FamilyContact": []
//    },
//    {
//      "Family_ID": "7",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "28/03/2018 18:18:02",
//      "Status_ID": "1",
//      "Contact_ID": "34",
//      "FamilyContact_ID": "6",
//      "ContactTypeName": "Self",
//      "FirstName": "family1",
//      "LastName": "family",
//      "EmailAddress": "family@gmail.com",
//      "Gender_ID": "1",
//      "DateofBirth": "28/03/2018 00:00:00",
//      "ContactPhoneNumber": "1234567890",
//      "FamilyContactCreateDate": "28/03/2018 18:18:02",
//      "FamilyContact": []
//    },
//    {
//      "Family_ID": "8",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "29/03/2018 14:45:19",
//      "Status_ID": "1",
//      "Contact_ID": "37",
//      "FamilyContact_ID": "9",
//      "ContactTypeName": "Self",
//      "FirstName": "test1",
//      "LastName": "testing",
//      "EmailAddress": "testing@gmail.com",
//      "Gender_ID": "2",
//      "DateofBirth": "29/03/2006 00:00:00",
//      "ContactPhoneNumber": "2589637410",
//      "FamilyContactCreateDate": "29/03/2018 14:45:19",
//      "FamilyContact": []
//    },
//    {
//      "Family_ID": "9",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "29/03/2018 14:50:43",
//      "Status_ID": "1",
//      "Contact_ID": "38",
//      "FamilyContact_ID": "10",
//      "ContactTypeName": "Self",
//      "FirstName": "test",
//      "LastName": "family",
//      "EmailAddress": "testfamily@gmail.com",
//      "Gender_ID": "1",
//      "DateofBirth": "29/03/2013 00:00:00",
//      "ContactPhoneNumber": "2580963741",
//      "FamilyContactCreateDate": "29/03/2018 14:50:43",
//      "FamilyContact": []
//    },
//    {
//      "Family_ID": "10",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "29/03/2018 14:57:36",
//      "Status_ID": "1",
//      "Contact_ID": "39",
//      "FamilyContact_ID": "11",
//      "ContactTypeName": "Self",
//      "FirstName": "android",
//      "LastName": "test",
//      "EmailAddress": "androidtestfamily@gmail.com",
//      "Gender_ID": "2",
//      "DateofBirth": "29/03/1998 00:00:00",
//      "ContactPhoneNumber": "1234567890",
//      "FamilyContactCreateDate": "29/03/2018 14:57:36",
//      "FamilyContact": [
//        {
//          "Contact_ID": "40",
//          "FamilyContact_ID": "12",
//          "ContactTypeName": "Child",
//          "FirstName": "first",
//          "LastName": "child",
//          "EmailAddress": "child@gmail.com",
//          "Gender_ID": "1",
//          "DateofBirth": "29/03/2009 00:00:00",
//          "ContactPhoneNumber": "1236547890",
//          "FamilyContactCreateDate": "29/03/2018 15:09:13"
//        },
//        {
//          "Contact_ID": "41",
//          "FamilyContact_ID": "13",
//          "ContactTypeName": "Child",
//          "FirstName": "second",
//          "LastName": "test",
//          "EmailAddress": "secondtest@gmail.com",
//          "Gender_ID": "2",
//          "DateofBirth": "29/03/2005 00:00:00",
//          "ContactPhoneNumber": "1236985470",
//          "FamilyContactCreateDate": "29/03/2018 15:22:47"
//        },
//        {
//          "Contact_ID": "42",
//          "FamilyContact_ID": "14",
//          "ContactTypeName": "Child",
//          "FirstName": "second",
//          "LastName": "test",
//          "EmailAddress": "secondtest@gmail.com",
//          "Gender_ID": "2",
//          "DateofBirth": "29/03/2005 00:00:00",
//          "ContactPhoneNumber": "1236985470",
//          "FamilyContactCreateDate": "29/03/2018 15:26:43"
//        }
//      ]
//    },
//    {
//      "Family_ID": "11",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "29/03/2018 15:50:56",
//      "Status_ID": "1",
//      "Contact_ID": "43",
//      "FamilyContact_ID": "15",
//      "ContactTypeName": "Self",
//      "FirstName": "jay",
//      "LastName": "patel",
//      "EmailAddress": "jay@patel.com",
//      "Gender_ID": "1",
//      "DateofBirth": "01/01/1992 00:00:00",
//      "ContactPhoneNumber": "9876546546",
//      "FamilyContactCreateDate": "29/03/2018 15:50:56",
//      "FamilyContact": []
//    },
//    {
//      "Family_ID": "12",
//      "AreSeparated": "True",
//      "FamilyCreateDate": "29/03/2018 16:39:34",
//      "Status_ID": "1",
//      "Contact_ID": "44",
//      "FamilyContact_ID": "16",
//      "ContactTypeName": "Self",
//      "FirstName": "jk",
//      "LastName": "patel",
//      "EmailAddress": "jk@jk.com",
//      "Gender_ID": "1",
//      "DateofBirth": "15/03/1990 00:00:00",
//      "ContactPhoneNumber": "1234567890",
//      "FamilyContactCreateDate": "29/03/2018 16:39:34",
//      "FamilyContact": []
//    }
//  ]
//}