# Comment Tree Backend

## Quick Start

### Development

Node version 14+ is required.

```
yarn install & yarn dev
```

### Docker

Under `deploy` folder

```
docker run --rm --name comments-tree-frontend \
            -v $(pwd)/comments.nginx.conf:/etc/nginx/conf.d/default.conf \
            -p 80:80 \
        wangding85/comments-tree-frontend
```

**or use docker-compose to start frontend and backend with single command**

```
docker-compose up
```

## Architecture
Based on NuxtJS, it's a scaffold for building SSR website, but I use it to generate a static SPA application.

- State Management
  - use `Vuex` to manipulate states in `LoginDialog` and `Snackbar`.

- Error handling
  - add an interceptor in `axios` plugin to catch all `401` errors and redirect to the login page.

- i18n
  - `@nuxtjs/i18n` provides the out-of-the-box i18n capability.
  - `zh_CN` has not been implemented yet.

- UI Library
  - use `Vuetify` 

## Known Issues
  - If the nested comment's depth is more than 30, the UI layout will get messed up 😫
