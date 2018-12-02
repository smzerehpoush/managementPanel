# policeHamrah
list of policeHamrah Systems :
base path : 192.168.250.53:9002/policeHamrah/api

01:(/login)@POST:login:login to police hamrah system

02:(/login/system?token&fkSystemId)@GET:loginToSystem:login to all other system except policeHamrah

03:(/logout?token&fkSystemId)@GET:logout:logout from systems with fkSystemId, if fkSystemId is null then logout from PH

04:(/privilege?token&fkSystemId)@GET:getUserPrivileges: get privileges of a user with token

05:(/role)@POST:addRole:add a role

06:(/role)@PUT:editRole:edit a role

07:(/role?token&fkRoleId)@DELETE:removeRole : remove role of user

08:(/role/privileges?token&fkRoleId)@GET:getRolePrivileges :get privileges of a role  

09:(/system/versions?token)@GET:getSystemWithVersions:list of systems with last version of them

10:(/system/users?token&fkSystemId)@GET:getSystemUsers:list of users a system 

11:(/token/remove?token&fkUserId&fkSystemId)@GET:removeToken:remove token of a user from systems

12:(/user)@POST:addUser: add a user

13:(/user)@PUT:editUser: edit a user

14:(/user/active?token&fkUserId&fkSystemId)@GET : activateUser : active a user

15:(/user/deActive?token&fkUserId&fkSystemId)@GET : deActiveUser : deactive a user

16:(/user/filter)@POST : filterUsers : filter users

17:(/user/resetPassword)@POST : resetPassword: reset password of a user

18:(/user/roles?token&fkSystemId)@GET : getUserRoles: get Roles of a use in 

19:(/user/roles/privileges?token&fkSystemId)@GET : getUserRolesWithPrivileges :  list of roles with privileges of a user

20:(/user/privileges?token&fkSystemId)@GET : getUserPrivileges : list if privileges of currentUser in specific system

21:(/user/systems?token)@GET : getUserSystems : list of systems of a specific user

22:(/verify)@GET verify web server

23:(/system/users)@POST:list of users a system

24:(/system/roles/privileges?token&fkSystemId)@GET:get roles with privileges of a system

25:(/user/roles)@POST:assign a role to a user

26:(/user/systems/assign)@POST: assign a user to other systems

27:(/system?token)@GET: list of all systems