const express = require("express");

const {
    createGroup,
    getGroups,
    searchGroups,
    getGroupById,
    updateGroup,
    deleteGroup,
    joinGroup,
    leaveGroup
} = require("../controllers/groupController");

const protect = require("../middleware/authMiddleware");

const router = express.Router();

// Public group routes
router.get("/", getGroups);
router.get("/search", searchGroups);
router.get("/:id", getGroupById);

// Protected group routes
router.post("/", protect, createGroup);

router
    .route("/:id")
    .put(protect, updateGroup)
    .delete(protect, deleteGroup);

router.post("/:id/join", protect, joinGroup);
router.post("/:id/leave", protect, leaveGroup);

module.exports = router;
