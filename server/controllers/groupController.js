const StudyGroup = require("../models/StudyGroup");
const asyncHandler = require("../utils/asyncHandler");

// Creates a new study group
const createGroup = asyncHandler(async (req, res) => {
    const { name, description, institution, course, category } = req.body;

    const group = await StudyGroup.create({
        name,
        description,
        institution,
        course,
        category,
        ownerId: req.user._id,
        members: [req.user._id]
    });

    const populatedGroup = await StudyGroup
        .findById(group._id)
        .populate("ownerId", "username fullName")
        .populate("members", "username fullName");

    res.status(201).json({
        success: true,
        group: populatedGroup
    });
});
// Returns all study groups
const getGroups = asyncHandler(async (req, res) => {
   const groups = await StudyGroup
       .find()
       .populate("ownerId", "username fullName")
       .populate("members", "username fullName")
       .sort({ createdAt: -1 });
    res.json({
        success: true,
        groups
    });
});
// Searches groups by institution, course and category
const searchGroups = asyncHandler(async (req, res) => {
    const { institution, course, category } = req.query;

    const filter = {};

    if (institution) filter.institution = new RegExp(institution, "i");
    if (course) filter.course = new RegExp(course, "i");
    if (category) filter.category = category;

    const groups = await StudyGroup
        .find(filter)
        .populate("ownerId", "username fullName")
        .populate("members", "username fullName")
        .sort({ createdAt: -1 });
    res.json({
        success: true,
        groups
    });
});
// Returns a single group by ID
const getGroupById = asyncHandler(async (req, res) => {
    const group = await StudyGroup
        .findById(req.params.id)
        .populate("ownerId", "username fullName")
        .populate("members", "username fullName");

    if (!group) {
        res.status(404);
        throw new Error("Study group not found");
    }

    res.json({
        success: true,
        group
    });
});
// Updates a study group owned by the authenticated user
const updateGroup = asyncHandler(async (req, res) => {
    const group = await StudyGroup.findById(req.params.id);

    if (!group) {
        res.status(404);
        throw new Error("Study group not found");
    }

    if (group.ownerId.toString() !== req.user._id.toString()) {
        res.status(403);
        throw new Error("Only the group owner can update this group");
    }

    const allowedFields = [
        "name",
        "description",
        "institution",
        "course",
        "category"
    ];

    allowedFields.forEach((field) => {
        if (req.body[field] !== undefined) {
            group[field] = req.body[field];
        }
    });

    await group.save();

    const populatedGroup = await StudyGroup
    .findById(group._id)
    .populate("ownerId", "username fullName")
    .populate("members", "username fullName");

res.json({
    success: true,
    group: populatedGroup
});
});
// Deletes a study group owned by the authenticated user
const deleteGroup = asyncHandler(async (req, res) => {
    const group = await StudyGroup.findById(req.params.id);

    if (!group) {
        res.status(404);
        throw new Error("Study group not found");
    }

    if (group.ownerId.toString() !== req.user._id.toString()) {
        res.status(403);
        throw new Error("Only the group owner can delete this group");
    }

    await group.deleteOne();

    res.json({
        success: true,
        message: "Study group deleted successfully"
    });
});
// Adds the authenticated user to a study group
const joinGroup = asyncHandler(async (req, res) => {
    const group = await StudyGroup.findById(req.params.id);

    if (!group) {
        res.status(404);
        throw new Error("Study group not found");
    }

    if (group.members.includes(req.user._id)) {
        res.status(409);
        throw new Error("User is already a member of this group");
    }

    group.members.push(req.user._id);
    await group.save();

    res.json({
        success: true,
        message: "Joined study group successfully"
    });
});

// Removes the authenticated user from a study group
const leaveGroup = asyncHandler(async (req, res) => {
    const group = await StudyGroup.findById(req.params.id);

    if (!group) {
        res.status(404);
        throw new Error("Study group not found");
    }

    if (group.ownerId.toString() === req.user._id.toString()) {
        res.status(400);
        throw new Error("Group owner cannot leave the group");
    }

    if (!group.members.includes(req.user._id)) {
        res.status(409);
        throw new Error("User is not a member of this group");
    }

    group.members.pull(req.user._id);
    await group.save();

    res.json({
        success: true,
        message: "Left study group successfully"
    });
});
module.exports = {
    createGroup,
    getGroups,
    searchGroups,
    getGroupById,
    updateGroup,
    deleteGroup,
    joinGroup,
    leaveGroup
};