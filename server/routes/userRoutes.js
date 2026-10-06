const express = require("express");
const {
    register,
    login,
    getUsers,
    getProfile,
    searchUsers,
    updateProfile,
    deleteProfile
} = require("../controllers/userController");

const protect = require("../middleware/authMiddleware");

const router = express.Router();

// Authentication routes
router.post("/register", register);
router.post("/login", login);

// User routes
router.get("/", getUsers);
router.get("/search", searchUsers);

router
    .route("/profile")
    .get(protect, getProfile)
    .put(protect, updateProfile)
    .delete(protect, deleteProfile);

module.exports = router;