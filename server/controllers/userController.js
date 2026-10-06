const jwt = require("jsonwebtoken");
const User = require("../models/User");
const asyncHandler = require("../utils/asyncHandler");

// Creates a signed JWT for the authenticated user
const generateToken = (userId) =>
    jwt.sign({ id: userId }, process.env.JWT_SECRET, { expiresIn: "7d" });

// Registers a new user
const register = asyncHandler(async (req, res) => {
    const { username, email, password, fullName, institution, fieldOfStudy, studyYear, bio } = req.body;

    const userExists = await User.findOne({
        $or: [{ email }, { username }]
    });

    if (userExists) {
        res.status(409);
        throw new Error("User with this email or username already exists");
    }

    const user = await User.create({
        username,
        email,
        password,
        fullName,
        institution,
        fieldOfStudy,
        studyYear,
        bio
    });

    res.status(201).json({
        success: true,
        user,
        token: generateToken(user._id)
    });
});

// Authenticates an existing user
const login = asyncHandler(async (req, res) => {
    const { email, password } = req.body;

    const user = await User.findOne({ email }).select("+password");

    if (!user || !(await user.comparePassword(password))) {
        res.status(401);
        throw new Error("Invalid email or password");
    }

    res.json({
        success: true,
        user,
        token: generateToken(user._id)
    });
});

// Returns all users
const getUsers = asyncHandler(async (req, res) => {
    const users = await User.find().sort({ createdAt: -1 });

    res.json({
        success: true,
        users
    });
});

// Returns the authenticated user's profile
const getProfile = asyncHandler(async (req, res) => {
    res.json({
        success: true,
        user: req.user
    });
});

// Searches users by institution, field of study and study year
const searchUsers = asyncHandler(async (req, res) => {
    const { institution, fieldOfStudy, studyYear } = req.query;

    const filter = {};

    if (institution) filter.institution = new RegExp(institution, "i");
    if (fieldOfStudy) filter.fieldOfStudy = new RegExp(fieldOfStudy, "i");
    if (studyYear) filter.studyYear = Number(studyYear);

    const users = await User.find(filter);

    res.json({
        success: true,
        users
    });
});

// Updates the authenticated user's profile
const updateProfile = asyncHandler(async (req, res) => {
    const allowedFields = [
        "username",
        "email",
        "fullName",
        "institution",
        "fieldOfStudy",
        "studyYear",
        "bio"
    ];

    allowedFields.forEach((field) => {
        if (req.body[field] !== undefined) {
            req.user[field] = req.body[field];
        }
    });

    await req.user.save();

    res.json({
        success: true,
        user: req.user
    });
});

// Deletes the authenticated user's account
const deleteProfile = asyncHandler(async (req, res) => {
    await req.user.deleteOne();

    res.json({
        success: true,
        message: "User deleted successfully"
    });
});
module.exports = {
    register,
    login,
    getUsers,
    getProfile,
    searchUsers,
    updateProfile,
    deleteProfile
};