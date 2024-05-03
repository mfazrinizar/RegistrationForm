![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Language](https://img.shields.io/badge/Language-Java-orange) [![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# RegistrationForm

Registration Form built using Java GUI Library and SQLite-based CloudFlare D1 Database (CRUD Operation).

## Overview
Main Form                  | Update Form
:-------------------------:|:-------------------------:|
![Main UI](https://github.com/mfazrinizar/RegistrationForm/blob/main/Screenshots/MainGUI.png?raw=true)|![Update Dialog](https://github.com/mfazrinizar/RegistrationForm/blob/main/Screenshots/updateDialog.png)|

## Features
- **User Text Input Fields**: The Registration Form includes fields for the user to input their name, email, password, country, province, and phone number. Each field is validated to ensure that the input is in the correct format and prevent security breach.
- **Password Confirmation**: The Registration Form includes a password confirmation field. The user must enter their password twice, and the form checks that the two entries match. This helps to prevent the user from making a mistake when entering their password.
- **Country Selection**: The Registration Form includes a combo box for the user to select their country. The combo box is populated with a list of countries, which makes it easy for the user to select their country.
- **Error Labels**: The Registration Form includes error labels next to each input field. If the user enters invalid input, the corresponding error label is displayed. This provides immediate feedback to the user and helps them to correct their input.
- **Credentials Environment**: The Registration Form uses a Credentials class to manage the API URL and bearer token. This makes it easy to change the API credentials without modifying the rest of the code.
- **Password Hashing**: The Registration Form uses a `PasswordHasher` class to hash the user's password using salt before it is stored. This improves the security of the form by ensuring that the user's actual password is not stored.
- **User Data Retrieval**: The Registration Form uses a `GetUserData` class to retrieve the user's data from the database. This allows the form to pre-fill the input fields with the user's existing data.
- **Password Strength Checking**: The form uses a `CheckPasswordUser` class to check the strength of the user's password. This helps to ensure that the user chooses a strong password.
- **User Data Updating**: The Registration Form includes a feature to update the user's data in the database. This allows the user to change their registration details after they have registered.
- **Asynchronous Operations**: The Registration Form performs database operations asynchronously. This prevents the form UI from freezing while it waits for a response from the database.
- **Confirmation Dialog**: The Registration Form includes a confirmation dialog that asks the user to confirm their email and password before their data is updated. This provides an additional layer of security by ensuring that the user is the one who initiated the update.

## CRUD (Create, Read, Update, and Delete) Operation
The Registration Form supports all four basic functions of persistent storage (CRUD):

1. **Create**: This operation is performed when the user clicks the "Daftar" or "Register" button. The button has an associated `ActionListener` (`RegisterButtonListener` class) that triggers the registration process. The user data is collected from the form fields, which include name, email, password, country, province, and phone number. The password is hashed using a secure hashing algorithm before being stored. The data is then sent to the database via an HTTP POST request, creating a new record in the database. If the operation is successful, a confirmation message is displayed to the user.

2. **Read**: This operation is performed when the user wants to view their existing registration details, for example when retrieving their registration details from Database via _Ambil Data_ or _Get Data_ Button. The `GetUserData` class is used to retrieve the user's data from the database. The user's email is used as the key to fetch the data. Once the data is retrieved, it is displayed in the form fields for the user to view.

3. **Update**: This operation is performed when the user wants to update their registration details, for example when updating their registration details from Database via _Ubah/Perbarui_ or _Change/Update_ Button. A dialog consisting of email and password textfield is displayed to the user. If the input is correct, the user can now see and modify the data in the form fields and clicks the _Ubah/Perbarui/Update_ button. The updated data is then sent to the database via an HTTP POST request, and the corresponding record in the database is updated. The `UpdateUserData` class is responsible for this operation.

4. **Delete**: This operation is performed when the user wants to delete their registration details, for example when updating their registration details from Database via _Hapus_ or _Delete_ Button. A dialog consisting of email and password textfield is displayed to the user. If the input is correct, a confirmation dialog is displayed to the user to confirm the deletion. If the user confirms, an HTTP DELETE request is sent to the database, and the corresponding record is deleted. The `DeleteUserData` class is responsible for this operation.

These operations are all performed **asynchronously** to prevent the UI from freezing during the database operations. The `SwingWorker` class is used to perform these operations in a background thread.

## How to Run
1. Clone this repository (using `git clone https://github.com/mfazrinizar/RegistrationForm.git` command or download in ZIP form).
2. Download Java JDK with minimum version `Java 11`.
3. I suggest opening the project in an IDE that fully supports Java, for example IntelliJ IDEA.
4. Sign Up for a CloudFlare account and create a `SQLite-based D1 Database` hosted by CloudFlare. The D1 should be located in `Workers & Pages` section. For more explanations, visit [here](https://developers.cloudflare.com/d1/get-started/). For D1 REST API Documentation, visit [here](https://developers.cloudflare.com/api/operations/cloudflare-d1-create-database).
5. Navigate to `src/main/java/com/github/mfazrinizar/RegistrationForm/Credentials` and open `.env.example` file. Delete the one line comment and change the value of each environment variables (the value must be encoded in Base64, you can use this [tool](https://base64.guru/converter/encode)). 
6. Rename `.env.example` file into `.env`.
7. You should be good to go. Run the `Main.java`'s `main` method.
