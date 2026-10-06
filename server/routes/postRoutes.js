const express = require("express");

const {
    createPost,
    getPosts,
    searchPosts,
    getPostById,
    updatePost,
    deletePost
} = require("../controllers/postController");

const protect = require("../middleware/authMiddleware");

const router = express.Router();

// Public post routes
router.get("/", getPosts);
router.get("/search", searchPosts);
router.get("/:id", getPostById);

// Protected post routes
router.post("/", protect, createPost);

router
    .route("/:id")
    .put(protect, updatePost)
    .delete(protect, deletePost);

module.exports = router;