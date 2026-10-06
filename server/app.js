const express = require("express");
const cors = require("cors");
const userRoutes = require("./routes/userRoutes");
const groupRoutes = require("./routes/groupRoutes");
const postRoutes = require("./routes/postRoutes");
const errorHandler = require("./middleware/errorMiddleware");

const app = express();

app.use(cors());
app.use(express.json());
app.use("/api/users", userRoutes);
app.use("/api/groups", groupRoutes);
app.use("/api/posts", postRoutes);
app.use(express.static("public"));

app.get("/api/health", (req, res) => {
    res.json({
        success: true,
        message: "AcademiLink API is running"
    });
});
app.use(errorHandler);

module.exports = app;
