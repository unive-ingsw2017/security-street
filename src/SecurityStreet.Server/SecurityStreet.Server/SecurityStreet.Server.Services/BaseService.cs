using SecurityStreet.Server.Models.Base;
using SecurityStreet.Server.Services.Models;
using ServiceStack;
using ServiceStack.Data;
using ServiceStack.OrmLite;
using System;
using System.Collections.Generic;

namespace SecurityStreet.Server.Services
{
    public class BaseService<TEntity, TDto, TReadRequest> : Service
        where TEntity : BaseEntityWithAutoIncrement
        where TDto : BaseEntity
        where TReadRequest : ReadRequest
    {
        /// <summary>
        /// The database connection factory
        /// </summary>
        protected IDbConnectionFactory dbConnectionFactory;

        /// <summary>
        /// Gets or sets the automatic query.
        /// </summary>
        /// <value>
        /// The automatic query.
        /// </value>
        public IAutoQueryDb AutoQuery { get; set; }

        /// <summary>
        /// Initializes a new instance of the <see cref="BaseCrudService{Entity, Dto, ReadRequest, UpdateRequest, DeleteRequest}"/> class.
        /// </summary>
        public BaseService()
        {
            dbConnectionFactory = HostContext.Resolve<IDbConnectionFactory>();
        }

        /// <summary>
        /// Gets the specified request.
        /// </summary>
        /// <param name="request">The request.</param>
        /// <returns></returns>
        /// <exception cref="System.ArgumentException">Request cannot be null</exception>
        public List<TDto> Any(TReadRequest request)
        {
            if (request == null)
                throw new ArgumentException("Request cannot be null");
            using (var db = dbConnectionFactory.Open())
            {
                try
                {
                    List<TEntity> entity;
                    if (request.Id <= 0)
                        entity = db.Select<TEntity>();
                    else
                        entity = db.Select<TEntity>(a => a.Id == request.Id);
                    
                    return entity.ConvertAll(a => a.ConvertTo<TDto>());
                }
                catch (Exception e)
                {
                    throw e;
                }
            }
        }

    }
}
