using Funq;
using ServiceStack;
using SecurityStreet.Server.Services.Autovelox;
using ServiceStack.Data;
using ServiceStack.OrmLite;
using SecurityStreet.Server.Models.Entities;

namespace SecurityStreet.Server
{
    //VS.NET Template Info: https://servicestack.net/vs-templates/EmptyAspNet
    public class AppHost : AppHostBase
    {
        /// <summary>
        /// Base constructor requires a Name and Assembly where web service implementation is located
        /// </summary>
        public AppHost()
            : base("SecurityStreet.Server", typeof(AutoveloxService).Assembly) { }

        /// <summary>
        /// Application specific configuration
        /// This method should initialize any IoC resources utilized by your web service classes.
        /// </summary>
        public override void Configure(Container container)
        {
            container.Register<IDbConnectionFactory>(new OrmLiteConnectionFactory("Data Source=VAIO-W10;Initial Catalog=Training;Integrated Security=True", SqlServerDialect.Provider));

            SetConfig(new HostConfig
            {
                HandlerFactoryPath = "api"
            });

            Plugins.Add(new AutoQueryFeature { MaxLimit = 1000 });
        }
    }
}