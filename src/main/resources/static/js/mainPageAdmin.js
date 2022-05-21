$(document).ready(async function () {

    const response = await fetch('http://localhost:8080/mainPage/api/getAuthUser/')
    const result = await response.json();
    document.querySelector('#navBarAdmin').innerHTML =
        `<a class="nav-link disabledclassName-white font-weight-bold" href="#"> ${result.userName}
            with roles: ${result.roles.map(role => role.role === 'ROLE_USER' ? 'USER' : 'ADMIN')} </a>`

    console.log(result)



    async function submit() {
        const response = await fetch('http://localhost:8080/mainPage/api/allUsers')
        const result = await response.json();
        // console.log(result)
        let roles = ""

        $.each(result, async function (index, value) {
            // console.log(value.id)

            $('.table #users_table').append("<tr>")
            // $.each(value, function (index, value1) {
            // console.log(value1)
            $('.table #users_table').append("<td>" + value.id + "</td>")
            $('.table #users_table').append("<td>" + value.userName + "</td>")
            $('.table #users_table').append("<td>" + value.age + "</td>")
            // })
            $.each(value.roles, function (index, value2) {
                roles += value2.role + "<br>"
            })
            $('.table #users_table').append("<td>" + roles + "</td>")
            roles = ""
            $('.table #users_table').append(
                $('<button/>', {
                    class: 'btn btn-primary',
                    text: 'Edit',
                    id: 'btn_edit',
                    click: async function (event) {
                        roles = ""
                        // console.log(value)
                        const href = await fetch('http://localhost:8080/mainPage/api/getUserById/' + value.id)
                        const hrefJson = await href.json()
                        // console.log(hrefJson)
                        event.preventDefault();
                        $('.myForm #userId').val(hrefJson.id)
                        $('.myForm #userName').val(hrefJson.userName);
                        $('.myForm #userAge').val(hrefJson.age);

                        $.each(hrefJson.roles, function (index, value) {
                            console.log(value)
                            if (value.role === "ROLE_ADMIN") {
                                $('.selDiv option[value="2"]').prop('selected', true);
                            }
                            if (value.role === "ROLE_USER") {
                                $('.selDiv option[value="1"]').prop('selected', true);
                            }
                        })
                        $('#exampleModal').modal()

                    }




                })
            // then(res=>document.getElementById('allUser-tab').click())



            )
            // document.getElementById("addUserBtn").onclick = function () {
            //     // location.href = "http://localhost:8080/mainPage"
            //     // document.querySelector(".nav-link active").dispatchEvent(new Event("click"))
            //     console.log("click")
            //     let btn = document.querySelector("#allUser-tab")
            //     btn.dispatchEvent(new Event('click'))
            // }
            // function triggerButton1Click() {
            //     document.querySelector("#addUserBtn").dispatchEvent(new Event("click"));
            // }

            $('.table #users_table').append(
                $('<button/>', {
                    class: 'btn btn-primary',
                    text: 'Delete',
                    id: 'btn_delete',
                    click: function (event) {
                        event.preventDefault();
                        $.ajax({
                            method: 'DELETE',
                            url: '/mainPage/api/delete/' + value.id,
                            headers: {'Content-type': 'application/json; charset=utf-8'},
                            success: function (response) {
                                $('#users_table').empty()
                                submit()

                            }
                        })
                    }

                })
            )

            $('.table #users_table').append("</tr>")


        })


    }

    submit()

    $('.addUserBtn').click(function (event) {
        event.preventDefault()

        $.ajax({
            type: 'POST',
            url: '/mainPage/api/users',
            data:
            JSON.stringify({
                userName: $('#exampleInputName').val(),
                age: $('#exampleInputAge').val(),
                password: $('#exampleInputPassword1').val(),
                roles: roleList('#exampleFormControlSelect2')
            }),
            traditional: true,
            headers: {'Content-type': 'application/json; charset=utf-8'},
            success: async function (response) {
                $('#users_table').empty()
                submit()
                $('#exampleInputName').val("")
                $('#exampleInputAge').val("")
                $('#exampleInputPassword1').val("")
                $('#exampleFormControlSelect2').val("")

            }
        }).then(res=>document.getElementById('allUser-tab').click())


    })

    $('.updateUserBtn').click(function (event) {
        event.preventDefault()
        $.ajax({
            type: 'PUT',
            url: '/mainPage',
            data: JSON.stringify({
                id: $('#userId').val(),
                userName: $('#userName').val(),
                age: $('#userAge').val(),
                password: $('#password').val(),
                roles: roleList('#roles')
            }),
            headers: {'Content-type': 'application/json; charset=utf-8'},
            success: async function (response) {
                $('#users_table').empty()
                submit()

            }
        })


    })

    let roleList = (controlSelect) => {
        let array = []
        let options = document.querySelector(controlSelect).options
        // console.log(options)

        for (let i = 0; i < options.length; i++) {
            if (options[i].selected) {
                let role = {id: options[i].value, role: options[i].text}
                // console.log(role)
                array.push(role)

            }
        }
        return array;
    }

})