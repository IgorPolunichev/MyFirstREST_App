$(document).ready(async function () {


    const response = await fetch('http://localhost:8080/userPage/api/getUserAuth')
    const result = await response.json();
    console.log(result)
    document.querySelector('#navBarUser').innerHTML =
        `<a class="nav-link disabledclassName-white font-weight-bold" href="#"> ${result.userName}
            with roles: ${result.roles.map(role => role.role === 'ROLE_USER' ? 'USER' : 'ADMIN')}  </a>`
})