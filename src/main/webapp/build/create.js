new HtmlWebpackPlugin({
  title: '移动_登录',
  filename: '../dist/static/html/mobile/Mobile_Login.html',
  template: 'html/template.html',
  inject: true,
  chunks: ['vendor', 'manifest', 'Mobile_Login'],
  minify: {
    removeComments: true,
    collapseWhitespace: true,
    removeAttributeQuotes: true
    // more options:
    // https://github.com/kangax/html-minifier#options-quick-reference
  },
  // necessary to consistently work with multiple chunks via CommonsChunkPlugin
  chunksSortMode: 'dependency'
}),
  new HtmlWebpackPlugin({
    title: '移动_管理_主页',
    filename: '../dist/static/html/mobile/manager/Mobile_Manager_Home.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Manager_Home'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_管理_学期',
    filename: '../dist/static/html/mobile/manager/Mobile_Manager_Semester.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Manager_Semester'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_管理_课程',
    filename: '../dist/static/html/mobile/manager/Mobile_Manager_Course.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Manager_Course'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_管理_学生',
    filename: '../dist/static/html/mobile/manager/Mobile_Manager_Student.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Manager_Student'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_管理_家长',
    filename: '../dist/static/html/mobile/manager/Mobile_Manager_Parent.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Manager_Parent'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_管理_教师',
    filename: '../dist/static/html/mobile/manager/Mobile_Manager_Teacher.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Manager_Teacher'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_管理_教师消息',
    filename: '../dist/static/html/mobile/manager/Mobile_Manager_TeacherMessage.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Manager_TeacherMessage'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_管理_班级',
    filename: '../dist/static/html/mobile/manager/Mobile_Manager_Room.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Manager_Room'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_教师_主页',
    filename: '../dist/static/html/mobile/Teacher/Mobile_Teacher_Home.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Teacher_Home'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_教师_课程',
    filename: '../dist/static/html/mobile/Teacher/Mobile_Teacher_Course.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Teacher_Course'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_教师_教师消息',
    filename: '../dist/static/html/mobile/Teacher/Mobile_Teacher_TeacherMessage.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Teacher_TeacherMessage'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_教师_学生',
    filename: '../dist/static/html/mobile/Teacher/Mobile_Teacher_Student.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Teacher_Student'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_家长_主页',
    filename: '../dist/static/html/mobile/parent/Mobile_Parent_Home.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Parent_Home'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  }),
  new HtmlWebpackPlugin({
    title: '移动_家长_学生',
    filename: '../dist/static/html/mobile/parent/Mobile_Parent_Student.html',
    template: 'html/template.html',
    inject: true,
    chunks: ['vendor', 'manifest', 'Mobile_Parent_Student'],
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
    },
    chunksSortMode: 'dependency'
  })
