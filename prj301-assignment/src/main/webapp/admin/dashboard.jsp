<%-- 
    Document   : dashboard
    Created on : Mar 19, 2023, 10:47:53 AM
    Author     : Admin
--%>

<%@page import="com.prj301.assignment.utils.AppPaths"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tag"%>

<%
	String logoutPath = request.getContextPath() + AppPaths.LOGOUT;
	String dashboardPath = request.getContextPath() + AppPaths.ADMIN;
	String usersPath = request.getContextPath() + AppPaths.ADMIN_USERS;
	String supportTicketsPath = request.getContextPath() + AppPaths.ADMIN_SUPPORT_TICKET;
%>

<!DOCTYPE html>
<html>
	<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<title>Admin - Dashboard</title>
		<!-- Boxicons -->
		<link href='https://unpkg.com/boxicons@2.0.9/css/boxicons.min.css' rel='stylesheet'>
		<!-- My CSS -->
    <tag:head
      alpine="true"
      cssPath="/assets/css/index.css"
      contextPath="${pageContext.request.contextPath}"
			/>
		<link rel="stylesheet" href="../assets/css/admin.css">
	</head>
	<body>
		<!-- SIDEBAR -->
		<section id="sidebar">
			<a href="#" class="brand">
				<i class='bx bxs-smile'></i>
				<span class="text">FlashWord</span>
			</a>
			<ul class="side-menu top">
				<li class="active">
					<a href="<%= dashboardPath%>">
						<i class='bx bxs-dashboard' ></i>
						<span class="text">Dashboard</span>
					</a>
				</li>
				<li>
					<a href="<%= usersPath%>">
						<i class='bx bxs-contact'></i>
						<span class="text">Users</span>
					</a>
				</li>
				<li>
					<a href="<%= supportTicketsPath%>">
						<i class='bx bxs-doughnut-chart' ></i>
						<span class="text">Support Tickets</span>
					</a>
				</li>
			</ul>
			<ul class="side-menu">
				<li>
					<a href="<%= logoutPath%>" class="logout">
						<i class='bx bxs-log-out-circle' ></i>
						<span class="text">Logout</span>
					</a>
				</li>
			</ul>
		</section>
		<!-- SIDEBAR -->



		<!-- CONTENT -->
		<section id="content">
			<!-- NAVBAR -->
			<nav>
				<i class='bx bx-menu' ></i>

				<div class="relative ml-3">
					<div class="peer h-full items-center flex">
						<button class="w-8 h-8 m-2 bg-gray-400 text-sm rounded-full flex items-center justify-center text-white focus:outline-none focus:shadow-solid">
							<img
								class="h-8 w-8 rounded-full"
								src="https://api.dicebear.com/5.x/adventurer/svg?seed=admin"
								alt="Profile Image"
								/>
						</button>
					</div>
					<div class="hidden peer-hover:block hover:block origin-top-right absolute right-0 z-10 pt-2 w-48 rounded-md shadow-lg">
						<div class="py-1 rounded-md bg-white shadow-xs">
							<a href="<%= logoutPath%>"
								 class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Logout</a>
						</div>
					</div>
				</div>
			</nav>
			<!-- NAVBAR -->

			<!-- MAIN -->
			<main>
				<div class="head-title">
					<div class="left">
						<ul class="breadcrumb">
							<li>
								<a href="#">Dashboard</a>
							</li>
							<li><i class='bx bx-chevron-right' ></i></li>
							<li>
								<a class="active" href="#">Home</a>
							</li>
						</ul>
					</div>
				</div>

				<ul class="box-info">
					<li class="shadow-md">
						<i class='bx bxs-group'></i>
						<span class="text">
							<h3>${requestScope.newUsersNum}</h3>
							<p>New User</p>
						</span>
					</li>
					<li class="shadow-md">
						<i class='bx bx-card'></i>
						<span class="text">
							<h3>${requestScope.newDecksNum}</h3>
							<p>New Deck</p>
						</span>
					</li>
					<li class="shadow-md">
						<i class='bx bxs-calendar-check'></i>
						<span class="text">
							<h3>${requestScope.decksStudiesNum}</h3>
							<p>Deck studies</p>
						</span>
					</li>
				</ul>


				<div class="table-data">
					<div class="order shadow-md">
						<div class="head">
							<h3>Support tickets</h3>
						</div>
						<table>
							<thead>
								<tr>
									<th>Email</th>
									<th>Created at</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${requestScope.supList}" var="sup">
									<tr>
										<td>
											<a href="<%= supportTicketsPath%>">${sup.senderEmail}</a>
										</td>
										<td>
											<a href="<%= supportTicketsPath%>">
												${sup.createdAt}
											</a>
										</td>
										<td>
											<span class="status process">${sup.status}</span>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</main>
			<!-- MAIN -->
		</section>
		<!-- CONTENT -->


		<script src="../assets/js/script.js"></script>
	</body>
</html>
