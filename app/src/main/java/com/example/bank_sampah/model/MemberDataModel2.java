package com.example.bank_sampah.model;

public class MemberDataModel2 {

        private String id;
        private String username;
        private String name;
        private String mail;
        private String phone;
        private String pass;
        private String status;

        public MemberDataModel2(String id, String username, String name, String mail, String phone, String pass, String status) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.mail = mail;
            this.phone = phone;
            this.pass = pass;
            this.status = status;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


}
