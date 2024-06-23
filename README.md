### It's not the final version of README file. I will finish it tomorrow :)

This is an example of how single permission can be handled when launching app or specific composable screen. <br>
Features to mention: <br>
- Permission launcher is launched at startup even if it's granted
- When showing permission dialog, user can't rotate screen or dismiss the dialog, clicking outside of it
- For checking permission when user navigated back from app settings, ActivityResultLauncher was used
<br>
How permissions work and why i decided to write logic like that:
<br>
In Android we have these permission properties:
<br>
1. Is it granted;<br>
2. Should Android show request rationale;<br>
3. Is it permanently declined (And it's hidden).<br><br>

As i know, there are 4 states of permission in app: <br>
| State №  | 1  | 2  | 3  | 4  |
|:----|:---:|:---:|:---:|:---:|
| Is it granted                         | false | false | false | true  |
| Should Android show request rationale | false | true  | false | false |
| Is it permanently declined            | false | false | true  | false |
<br>
States description:<br>
1. Permission wasn't granted and requested;<br>
2. Permission was declined in first request dialog. In Android version < 11 there can be multiple declines; <br>
3. Permission was declined in second request dialog. In Android version < 11 user should click "Never ask again"; <br>
4. Permission was granted. <br>


<br> (NEEDED TO EDIT)
Looking at the table and knowing, that we can't get, whether permission was permanently declined or not, we get to the weird thing: we can't differentiate states 1 and 3
