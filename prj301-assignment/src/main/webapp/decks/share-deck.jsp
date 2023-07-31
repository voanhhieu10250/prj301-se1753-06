<%-- 
    Document   : share-deck
    Created on : Mar 13, 2023, 9:10:56 AM
    Author     : hieunghia
--%>

<%@page import="com.prj301.assignment.javabean.DeckDetailsBean"%>
<%@page import="com.prj301.assignment.utils.AppPaths"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@taglib tagdir="/WEB-INF/tags/" prefix="tag"%>

<%
	DeckDetailsBean deckInfo = (DeckDetailsBean) request.getAttribute("deckInfo");

	pageContext.setAttribute("deck", deckInfo);
	pageContext.setAttribute("searchPath", request.getContextPath() + AppPaths.SHARE_DECK);
	pageContext.setAttribute("shareDeck", request.getContextPath() + AppPaths.SHARE_DECK + "?id=");
	pageContext.setAttribute("deckDetailsPath", request.getContextPath() + AppPaths.DECK_DETAILS + "?id=" + deckInfo.getDeckId());
	pageContext.setAttribute("userDetailsPath", request.getContextPath() + AppPaths.USER_DETAILS + "?email=");
%>
<jsp:useBean id="deck" class="com.prj301.assignment.javabean.DeckDetailsBean" scope="page" />
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8"/>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<title>FlashWord - Share deck</title>
		<tag:head
			alpine="true"
			icon="true"
			cssPath="/assets/css/index.css"
			contextPath="${pageContext.request.contextPath}"
			/>
	</head>
	<body>
		<!-- Navbar -->
		<jsp:include page="../WEB-INF/jspf/navbar.jsp" flush="true"/>

		<!-- Share deck -->
		<div class="container py-8 md:py-12 px-6 mx-auto flex-1" x-data>
			<main class="max-w-5xl mx-auto p-6 bg-white rounded-lg shadow-md">
				<h2 class="text-2xl font-bold mb-4 text-center">Share your deck</h2>
				<div class="grid grid-cols-6 gap-4">
					<div class="col-span-6 flex justify-between items-start">
						<div class="overflow-hidden">
							<p class="block text-2xl font-bold text-gray-800 mb-2 pr-4 truncate" title="${deck.name}">${deck.name}<span 
									class="inline-flex relative top-[-4px] items-center align-middle ml-3 px-3 py-0.5 rounded-full text-sm font-medium" 
									x-bind:class="${deck.privateDeck == 0} ? 'bg-green-100 text-green-800 border-2 border-green-800' : 'bg-red-100 text-red-800 border-2 border-red-800'"
									x-text="${deck.privateDeck == 0} ? 'Public' : 'Private'">
								</span>
							</p>
							<p class="text-gray-500 text-base font-medium mb-4 break-words">${deck.description}</p>
						</div>
					</div>
					<div class="col-span-6 border"></div>

					<div class="col-span-6 md:col-span-4 md:pr-4">
						<form class="relative mb-2" action="${pageScope.searchPath}" method="GET">
							<input id="id" name="id" required hidden value="${pageScope.deck.deckId}"/>
							<input type="text" 
										 class="w-96 max-w-full border-gray-300 rounded-full py-2 px-4 leading-tight pl-12 border-0 ring-1 ring-inset ring-gray-300 outline-0 focus:ring-1 focus:ring-inset focus:ring-indigo-600" 
										 placeholder="Search user (type text, then press Enter)" 
										 name="search"
										 value="${requestScope.search}">
							<button class="absolute inset-y-0 left-0 flex items-center pl-4" type="submit" title="Search">
								<i class="fa-solid fa-magnifying-glass"></i>
							</button>
						</form>
						<div class="mb-2">
							<c:if test="${not empty search}">
								<p class="font-bold text-xl">${requestScope.userList.size()} results for "${requestScope.search}"</p>
							</c:if>
						</div>
						<form class="block" action="${pageScope.shareDeck}${pageScope.deck.deckId}" method="POST">
							<input name="deckId" required hidden value="${pageScope.deck.deckId}"/>
							<input name="search" required hidden value="${not empty requestScope.search? requestScope.search : ' '}"/>
							<div class="grid grid-cols-2 gap-3 md:gap-4">
								<c:if test="${not empty userList}">
									<c:forEach items="${userList}" var="user">
										<div class="col-span-2 md:col-span-1">
											<input type="checkbox" 
														 id="${user.userId}-label"
														 value="${user.userId}"
														 name="selectedUsers" 
														 class="hidden peer" />
											<label for="${user.userId}-label" class="rounded-lg p-3 cursor-pointer flex justify-center items-center peer-checked:bg-gray-300">
												<div class="flex items-center gap-2 overflow-hidden flex-1">
													<img
														class="h-9 w-9 bg-gray-400 rounded-full mx-auto"
														src="${user.avatar}"
														alt="Profile Image"
														/>
													<div class="text-lg font-semibold overflow-hidden flex-1">
														<p class="font-bold truncate">${user.name}</p>
														<p class="text-gray-500 text-sm truncate">${user.email}</p>
													</div>
												</div>
											</label>
										</div>
									</c:forEach>
								</c:if>
							</div>
							<div class="mt-4 flex justify-end items-center gap-5">
								<a href="${pageScope.deckDetailsPath}" class="text-indigo-600 font-semibold hover:text-indigo-500 hover:underline hover:underline-offset-2" type="button">
									Back to details
								</a>
								<button class="bg-indigo-600 hover:bg-indigo-500 text-white font-bold py-2 px-4 rounded focus:shadow-outline" type="submit">Share</button>
							</div>
						</form>
					</div>
					<div class="col-span-6 md:col-span-2 md:pl-8 md:border-l-2 md:border-gray-300">
						<span class="block text-gray-700 font-bold mb-2">
							Shared users:
						</span>
						<div class="border rounded w-full py-2 px-3 text-gray-700 leading-tight border-0 ring-1 ring-inset ring-gray-300 outline-0">

							<c:forEach items="${sharedUsersList}" var="sharedUser">
								<div class="font-semibold rounded-lg p-1 mb-2 flex items-center border-0 ring-1 ring-inset ring-gray-300 outline-0 hover:ring-1 hover:ring-inset hover:ring-indigo-600">
									<div class="flex items-center">
										<a class="block p-1 truncate hover:font-bold" href="${pageScope.userDetailsPath}${sharedUser.email}">${sharedUser.name}</a>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
			</main>
		</div>

		<!-- Footer -->
		<jsp:include page="../WEB-INF/jspf/footer.jsp" flush="true"/>
		<jsp:include page="../WEB-INF/jspf/notification.jsp" flush="true"/>

	</body>
</html>
