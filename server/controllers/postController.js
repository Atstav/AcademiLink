const Post = require("../models/Post");
const asyncHandler = require("../utils/asyncHandler");

// Creates a new post
const createPost = asyncHandler(async (req, res) => {
    const { content, category, groupId } = req.body;

    const post = await Post.create({
        content,
        category,
        groupId,
        authorId: req.user._id
    });

    const populatedPost = await Post.findById(post._id)
        .populate("authorId", "username fullName")
        .populate("groupId", "name course institution");

    res.status(201).json({
        success: true,
        post: populatedPost
    });
});

// Returns all posts
const getPosts = asyncHandler(async (req, res) => {
    const posts = await Post
        .find()
        .populate("authorId", "username fullName")
        .populate("groupId", "name course institution")
        .sort({ createdAt: -1 });

    res.json({
        success: true,
        posts
    });
});

// Searches posts by group, category and date range
const searchPosts = asyncHandler(async (req, res) => {
    const { groupId, category, dateFrom, dateTo } = req.query;

    const filter = {};

    if (groupId) filter.groupId = groupId;
    if (category) filter.category = category;

    if (dateFrom || dateTo) {
        filter.createdAt = {};

        if (dateFrom) {
            filter.createdAt.$gte = new Date(dateFrom);
        }

        if (dateTo) {
            filter.createdAt.$lte = new Date(dateTo);
        }
    }

    const posts = await Post
        .find(filter)
        .populate("authorId", "username fullName")
        .populate("groupId", "name course institution")
        .sort({ createdAt: -1 });

    res.json({
        success: true,
        posts
    });
});

// Returns a single post by ID
const getPostById = asyncHandler(async (req, res) => {
    const post = await Post
        .findById(req.params.id)
        .populate("authorId", "username fullName")
        .populate("groupId", "name course institution");

    if (!post) {
        res.status(404);
        throw new Error("Post not found");
    }

    res.json({
        success: true,
        post
    });
});

// Updates a post owned by the authenticated user
const updatePost = asyncHandler(async (req, res) => {
    const post = await Post.findById(req.params.id);

    if (!post) {
        res.status(404);
        throw new Error("Post not found");
    }

    if (post.authorId.toString() !== req.user._id.toString()) {
        res.status(403);
        throw new Error("Only the post author can update this post");
    }

    const allowedFields = ["content", "category", "groupId"];

    allowedFields.forEach((field) => {
        if (req.body[field] !== undefined) {
            post[field] = req.body[field];
        }
    });

    await post.save();

    const updatedPost = await Post.findById(post._id)
        .populate("authorId", "username fullName")
        .populate("groupId", "name course institution");

    res.json({
        success: true,
        post: updatedPost
    });
});

// Deletes a post owned by the authenticated user
const deletePost = asyncHandler(async (req, res) => {
    const post = await Post.findById(req.params.id);

    if (!post) {
        res.status(404);
        throw new Error("Post not found");
    }

    if (post.authorId.toString() !== req.user._id.toString()) {
        res.status(403);
        throw new Error("Only the post author can delete this post");
    }

    await post.deleteOne();

    res.json({
        success: true,
        message: "Post deleted successfully"
    });
});

module.exports = {
    createPost,
    getPosts,
    searchPosts,
    getPostById,
    updatePost,
    deletePost
};