using SecurityStreet.Server.Models.Base;
using SecurityStreet.Server.Services.Models;
using ServiceStack;
using ServiceStack.Data;
using ServiceStack.OrmLite;
using System;
using System.Collections.Generic;

namespace SecurityStreet.Server.Services
{
    public class BaseCrudService<TEntity, TDto, TReadRequest, TUpdateRequest, TDeleteRequest> : Service
        where TEntity : BaseEntityWithAutoIncrement
        where TDto : BaseEntity
        where TReadRequest : ReadRequest
        where TUpdateRequest : UpdateOrCreateRequest<TDto>
        where TDeleteRequest : DeleteRequest
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
        public BaseCrudService()
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
                    List<TEntity> autovelox;
                    if (request.Id <= 0)
                        autovelox = db.Select<TEntity>();
                    else
                        autovelox = db.Select<TEntity>(a => a.Id == request.Id);
                    
                    return autovelox.ConvertAll(a => a.ConvertTo<TDto>());
                }
                catch (Exception e)
                {
                    throw e;
                }
            }
        }

        /// <summary>
        /// Deletes the specified request.
        /// </summary>
        /// <param name="request">The request.</param>
        /// <exception cref="System.ArgumentException"></exception>
        public void Delete(TDeleteRequest request)
        {
            if (request == null)
                throw new ArgumentException("Request cannot be null");

            try
            {
                using (var db = dbConnectionFactory.Open())
                {
                    db.Delete<TEntity>(a => a.Id == request.Id);
                }
            }
            catch (Exception e)
            {
                throw e;
            }
        }

        /// <summary>
        /// Puts the specified request.
        /// </summary>
        /// <param name="request">The request.</param>
        /// <returns></returns>
        /// <exception cref="System.ArgumentException"></exception>
        public TDto Any(TUpdateRequest request)
        {
            if (request == null)
                throw new ArgumentException("Request cannot be null");

            try
            {
                TDto result = null;
                TEntity requestEntity = request.Item.ConvertTo<TEntity>();
                using (var db = dbConnectionFactory.Open())
                {
                    if (requestEntity.Id > 0)
                        db.Save(requestEntity);
                    else
                        requestEntity.Id = (int)db.Insert(requestEntity, selectIdentity: true);

                    result = db.SingleById<TEntity>(requestEntity.Id)?.ConvertTo<TDto>();
                }
                return result;
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }
}
