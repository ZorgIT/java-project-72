@import hexlet.code.models.Page
@import gg.jte.Content
@import io.javalin.http.Context

@param Content content
@param Page page
@param Content footer
@param Context ctx

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${page.getTitle()}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
          crossorigin="anonymous">
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        .content-wrapper {
            flex: 1;
        }

        @supports not (min-height: 100dvh) {
            html { height: 100%; }
            body { min-height: 100%; }
        }

        .--brisk-axd {
            filter: blur(4px);
            user-select: none;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Анализатор страниц</a>
        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarNav"
                aria-controls="navbarNav"
                aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <div class="navbar-nav">
                <a class="nav-link" href="/">Главная</a>
                <a class="nav-link" href="/urls">Сайты</a>
            </div>
        </div>
    </div>
</nav>
<main class="content-wrapper">
    @if(page instanceof hexlet.code.dto.MainPage && !((hexlet.code.dto.MainPage) page).isVisited())
        <h1>Привет, c Хекслет!</h1>
        <p>Это сообщение показывается только один раз. Тест куки.</p>
    @endif
    @if(page != null && page.getFlash() != null)
<%--
TODO доработать  блок вывода флеш
<p>${page.getFlash()}</p>
сообщений
--%>
    @endif
    <div class="content">
        ${content}
    </div>
</main>


@if (footer != null)
    <div class="footer">
        ${footer}
    </div>
@else
    <footer class="footer border-top py-3 bg-light"> <!-- Убрали mt-5 -->
        <div class="container-xl">
            <div class="text-center">
                created by
                <a href="https://github.com/ZorgIT" target="_blank">ZorgIT</a>
            </div>
        </div>
    </footer>
@endif

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"
        integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13"
        crossorigin="anonymous"></script>
</body>
</html>