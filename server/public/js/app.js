$(document).ready(function () {
  $("#loadUsersBtn").click(function () {
    $.ajax({
      url: "/api/users",
      method: "GET",

      success: function (response) {
        $("#usersList").empty();

        response.users.forEach(function (user) {
          $("#usersList").append(`
                        <div class="card">
                            <strong>${user.fullName}</strong>
                            <p>Username: ${user.username}</p>
                            <p>Institution: ${user.institution}</p>
                            <p>Field: ${user.fieldOfStudy}</p>
                            <p>Study Year: ${user.studyYear}</p>
                        </div>
                    `);
        });
      },

      error: function () {
        $("#usersList").html("<p>Failed to load users.</p>");
      },
    });
  });

  $("#loadGroupsBtn").click(function () {
    $.ajax({
      url: "/api/groups",
      method: "GET",

      success: function (response) {
        $("#groupsList").empty();

        response.groups.forEach(function (group) {
          $("#groupsList").append(`
                        <div class="card">
                            <strong>${group.name}</strong>
                            <p>Institution: ${group.institution}</p>
                            <p>Course: ${group.course}</p>
                            <p>Category: ${group.category}</p>
                        </div>
                    `);
        });
      },

      error: function () {
        $("#groupsList").html("<p>Failed to load groups.</p>");
      },
    });
  });

  $("#loadPostsBtn").click(function () {
    $.ajax({
      url: "/api/posts",
      method: "GET",

      success: function (response) {
        $("#postsList").empty();

        response.posts.forEach(function (post) {
          $("#postsList").append(`
                        <div class="card">
                            <strong>${post.category}</strong>
                            <p>${post.content}</p>
                            <p>Author: ${post.authorId?.username || "Unknown"}</p>
                        </div>
                    `);
        });
      },

      error: function () {
        $("#postsList").html("<p>Failed to load posts.</p>");
      },
    });
  });
  
  $("#loadChartsBtn").click(function () {
    $.ajax({
      url: "/api/users",
      method: "GET",

      success: function (response) {
        drawUsersChart(response.users);
      },
    });

    $.ajax({
      url: "/api/posts",
      method: "GET",

      success: function (response) {
        drawPostsChart(response.posts);
      },
    });
  });
});
function drawUsersChart(users) {
  $("#usersChart").empty();

  const counts = d3.rollups(
    users,
    (values) => values.length,
    (user) => user.studyYear,
  );

  drawBarChart("#usersChart", counts, "Study Year");
}

function drawPostsChart(posts) {
  $("#postsChart").empty();

  const counts = d3.rollups(
    posts,
    (values) => values.length,
    (post) => post.category,
  );

  drawBarChart("#postsChart", counts, "Category");
}
function drawBarChart(selector, data, label) {
  const width = 600;
  const height = 300;
  const margin = {
    top: 20,
    right: 20,
    bottom: 60,
    left: 50,
  };

  const svg = d3
    .select(selector)
    .append("svg")
    .attr("width", width)
    .attr("height", height);

  const x = d3
    .scaleBand()
    .domain(data.map((item) => item[0]))
    .range([margin.left, width - margin.right])
    .padding(0.2);

  const y = d3
    .scaleLinear()
    .domain([0, d3.max(data, (item) => item[1]) || 1])
    .nice()
    .range([height - margin.bottom, margin.top]);

  svg
    .append("g")
    .attr("transform", `translate(0,${height - margin.bottom})`)
    .call(d3.axisBottom(x));

  svg
    .append("g")
    .attr("transform", `translate(${margin.left},0)`)
    .call(d3.axisLeft(y).ticks(5));

  svg
    .selectAll(".bar")
    .data(data)
    .enter()
    .append("rect")
    .attr("class", "bar")
    .attr("x", (item) => x(item[0]))
    .attr("y", (item) => y(item[1]))
    .attr("width", x.bandwidth())
    .attr("height", (item) => height - margin.bottom - y(item[1]));

  svg
    .selectAll(".value")
    .data(data)
    .enter()
    .append("text")
    .attr("class", "value")
    .attr("x", (item) => x(item[0]) + x.bandwidth() / 2)
    .attr("y", (item) => y(item[1]) - 5)
    .attr("text-anchor", "middle")
    .text((item) => item[1]);
}
