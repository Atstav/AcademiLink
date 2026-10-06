const jwt = require("jsonwebtoken");
const User = require("../models/User");
const asyncHandler = require("../utils/asyncHandler");

// Verifies the JWT and attaches the authenticated user to the request
const protect = asyncHandler(async (req, res, next) => {
    const token = req.headers.authorization?.split(" ")[1];

    if (!token) {
        res.status(401);
        throw new Error("Authentication required");
    }

    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    const user = await User.findById(decoded.id);

    if (!user) {
        res.status(401);
        throw new Error("User no longer exists");
    }

    req.user = user;
    next();
});

module.exports = protect;