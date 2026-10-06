# AcademiLink

AcademiLink is a student social networking application designed to help students connect, communicate, join study groups, share posts, and collaborate academically.

The project includes an Android application, a Node.js + Express backend, MongoDB database integration, a web interface using jQuery and Ajax, real-time chat with Socket.io, React media components, CSS3 features, and D3.js statistics.

## Main Features

- User registration and login
- User profile management
- Update and delete profile
- View and search users
- Study group creation and management
- Join and leave study groups
- Search study groups by multiple parameters
- Create, update, delete, list, and search posts
- Real-time user-to-user chat using Socket.io
- Web interface using jQuery and Ajax
- Dynamic statistics using D3.js
- React component with Video and Canvas
- Client-side and server-side validation
- Authorization and ownership checks

## Technologies

### Android Client
- Kotlin
- Jetpack Compose
- MVVM architecture
- Retrofit
- LiveData
- DataStore
- Socket.io Client

### Backend
- Node.js
- Express.js
- MongoDB
- Mongoose
- JWT Authentication
- bcrypt
- Socket.io

### Web Client
- HTML5
- CSS3
- JavaScript
- jQuery
- Ajax
- React
- D3.js

## Project Structure

```text
AcademiLink/
├── android/
│   ├── app/
│   ├── gradle/
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── gradle.properties
│
├── server/
│   ├── config/
│   ├── controllers/
│   ├── middleware/
│   ├── models/
│   ├── routes/
│   ├── utils/
│   ├── public/
│   │   ├── css/
│   │   ├── js/
│   │   ├── media/
│   │   └── index.html
│   ├── app.js
│   ├── server.js
│   └── package.json
│
└── .gitignore
```

## Main Models

The system uses three main models:

### User
Supports:
- Create
- Update
- Delete
- List
- Search

Main fields include:
- Username
- Email
- Full name
- Institution
- Field of study
- Study year
- Bio

### Study Group
Supports:
- Create
- Update
- Delete
- List
- Search
- Join group
- Leave group

Main fields include:
- Group name
- Description
- Institution
- Course
- Category
- Owner
- Members

### Post
Supports:
- Create
- Update
- Delete
- List
- Search

Main fields include:
- Content
- Category
- Author
- Study group
- Creation date

## Search Functionality

The application includes several multi-parameter search options.

Examples:

### User Search
- Institution
- Field of Study
- Study Year

### Study Group Search
- Institution
- Course
- Category

### Post Search
- Group
- Category
- Date range

## Authentication and Authorization

The application uses JWT-based authentication.

Protected functionality includes:
- Profile management
- Group creation and management
- Post creation and management
- User-specific actions

Users can only edit or delete content they are authorized to manage.

## Real-Time Chat

AcademiLink includes real-time user-to-user chat using Socket.io.

The Android client connects to the Node.js Socket.io server and supports:

- User-specific socket rooms
- Real-time message sending
- Real-time message receiving

The chat was tested successfully between two Android emulator instances.

## Web Interface

The backend also serves a web interface from:

```text
server/public/
```

The web interface uses jQuery and Ajax to retrieve live data from the backend API.

It includes:

- User list
- Study group list
- Post list
- Dynamic statistics
- React media demonstration

## D3.js Statistics

The project includes two dynamic D3.js charts using live database data:

### Users by Study Year
Displays the number of users for each study year.

### Posts by Category
Displays the number of posts for each category.

The chart data is retrieved dynamically through the backend API.

## React Features

The web client includes a React component demonstrating:

### Video
A promotional AcademiLink video is displayed using the HTML5 Video element.

### Canvas
A Canvas element is rendered through React and displays AcademiLink branding.

## CSS3 Features

The web interface includes the required CSS3 features:

- `text-shadow`
- `transition`
- multiple columns
- `@font-face`
- `border-radius`

## Running the Backend

Open a terminal inside the `server` directory:

```bash
cd server
npm install
npm run dev
```

The server runs by default on:

```text
http://localhost:3000
```

The web interface can also be accessed at:

```text
http://
